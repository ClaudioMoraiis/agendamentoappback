package com.example.demo.Usuario;

import com.example.demo.Jwt.TokenService;
import com.example.demo.UsuarioToken.UsuarioTokenRepository;
import com.example.demo.UsuarioToken.UsuarioTokenVO;
import com.example.demo.Util.ApiResponseUtil;
import com.example.demo.Util.Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository fRepository;

    @Autowired
    private PasswordEncoder fPasswordEncoder;

    @Autowired
    private TokenService fTokenService;

    @Autowired
    private AuthenticationManager fAuthenticationManager;

    @Autowired
    private UsuarioTokenRepository fUsuarioTokenRepository;


    public ResponseEntity<?>Validar(UsuarioCadastroDTO mDto, Long mId){
        if ((fRepository.findById(mId) == null) && (mId != null && (mId != 0))){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Nenhum cliente localizado com esse ID!")
            );
        }

        if (!Util.validaTelefone(mDto.getCelular())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "Número do celular inválido, verifique!"));
        }

        if (Util.formatarCpf(mDto.getCpf()).length() != 14) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "Cpf inválido, verifique!"));
        }

        if (fRepository.existsByEmailAndIdNot(mDto.getEmail(), mId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    ApiResponseUtil.response("Erro", "E-mail já em uso, verifique!"));
        }


        if (fRepository.existsByCpfAndIdNot(Util.formatarCpf(mDto.getCpf()), mId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    ApiResponseUtil.response("Erro", "Cpf já em uso, verifique!"));
        }

        if (mDto.getEmail() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "E-mail não informado!"));
        }

        if (fRepository.findByNome(mDto.getNome().toUpperCase()) != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "Já existe cliente com esse nome!"));
        }

        return null;
    }

    public ResponseEntity<?> cadastrar(UsuarioCadastroDTO mDto) {
        ResponseEntity mValidacao = Validar(mDto, 0L);
        if (mValidacao != null){
            return mValidacao;
        }

        UsuarioVO mUsuarioVO = new UsuarioVO();
        mUsuarioVO.setCpf(Util.formatarCpf(mDto.getCpf()));
        mUsuarioVO.setCelular(Util.formatarTelefone(mDto.getCelular()));
        mUsuarioVO.setEmail(mDto.getEmail().toUpperCase());
        mUsuarioVO.setNome(mDto.getNome().toUpperCase());

        String mSenha = fPasswordEncoder.encode(mDto.getSenha());
        mUsuarioVO.setSenha(mSenha);
        try {
            fRepository.save(mUsuarioVO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "Falha ao cadastrar usuário\n" + e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponseUtil.response("Sucesso", "Usuário cadastrado com sucesso"));
    }

    ;

    public ResponseEntity<?> login(String mEmail, String mSenha) {
        try {
            String mEmailUpper = mEmail.toUpperCase();
            UsuarioVO mVO = fRepository.findByEmail(mEmailUpper);
            if (fRepository.findByEmail(mEmailUpper) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ApiResponseUtil.response("Erro", "E-mail ou senha incorreto!"));
            }

            var mAuthToken = new UsernamePasswordAuthenticationToken(mEmailUpper, mSenha);
            var mAuth = fAuthenticationManager.authenticate(mAuthToken);

            var mToken = fTokenService.generateToken((UsuarioVO) mAuth.getPrincipal());
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponseUtil.response("Sucesso", "Login realizado com sucesso\n" + "Token: " + mToken));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponseUtil.response("Erro", e.getMessage()));
        }
    }

    public ResponseEntity<?> BuscarPorUsuarioToken(String mToken) {
        Optional<UsuarioTokenVO> mUsuarioTokenVO = fUsuarioTokenRepository.findByToken(mToken);
        if (mUsuarioTokenVO.isEmpty() || mUsuarioTokenVO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "Token vazio ou não informado")
            );
        }

        if (mUsuarioTokenVO.get().getUto_dthr_expiracao().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "Token expirou")
            );

        }

        return ResponseEntity.status(HttpStatus.OK).body(
                mUsuarioTokenVO.get().getUsuario()
        );
    }

    public List<Map<String, Object>> listar() {
        List<UsuarioVO> mUsuarioVO = fRepository.findAll();
        return mUsuarioVO.stream()
                .map(mVO -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("nome", mVO.getNome());
                    map.put("email", mVO.getEmail());
                    map.put("celular", mVO.getCelular());
                    map.put("cpf", mVO.getCpf());
                    map.put("id", mVO.getId());
                    return map;
                })
                .toList();
    }

    public ResponseEntity<?>alterar(UsuarioCadastroDTO mDto, Long mId){
        Optional<UsuarioVO> mUsuarioVO = fRepository.findById(mId);
        UsuarioVO mUsuarioVoOld = new UsuarioVO();
        BeanUtils.copyProperties(mUsuarioVO.get(), mUsuarioVoOld);

        ResponseEntity mValidacao = Validar(mDto, mId);
        if (mValidacao != null){
            return mValidacao;
        }

        UsuarioVO mVO = mUsuarioVO.get();
        mVO.setNome(mDto.getNome());
        mVO.setEmail(mDto.getEmail());
        mVO.setCpf(mDto.getCpf());
        mVO.setCelular(mDto.getCelular());

        if (mUsuarioVoOld.equals(mVO)){
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponseUtil.response("Sucesso", "Usuário alterado com sucesso"));
        }

        try {
            fRepository.save(mVO);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponseUtil.response("Sucesso", "Usuário alterado com sucesso"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", e.getMessage()));
        }
    }

    public ResponseEntity<?>deletar(Long mId){
        Optional<UsuarioVO> mVo = fRepository.findById(mId);
        if ((mVo.isEmpty()) || (mVo == null)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Nenhum usuário localizado com esse ID"));
        }

        try {
            fRepository.delete(mVo.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponseUtil.response("Sucesso", "Usuário excluido com sucesso"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", e.getMessage()));
        }
    }

    public ResponseEntity<?>getIdByName(String mName){
        UsuarioVO mUserVO = fRepository.findByNome(mName.toUpperCase());
        if (mUserVO == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Nenhum cliente localizado com esse nome"));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponseUtil.response("Sucesso", mUserVO.getId().toString()));
    }

}
