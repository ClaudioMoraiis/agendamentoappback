package com.example.demo.Usuario;

import com.example.demo.Jwt.TokenService;
import com.example.demo.UsuarioToken.UsuarioTokenRepository;
import com.example.demo.UsuarioToken.UsuarioTokenVO;
import com.example.demo.Util.ApiResponseUtil;
import com.example.demo.Util.Util;
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

    public ResponseEntity<?> cadastrar(UsuarioCadastroDTO mDto) {
        if (!Util.validaTelefone(mDto.getCelular())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "Número do celular inválido, verifique!"));
        }

        if (Util.formatarCpf(mDto.getCpf()).length() < 11){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "Cpf inválido, verifique!"));
        }

        if (fRepository.findByEmail(mDto.getEmail()) != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "E-mail já em uso, verifique!"));
        }


        if (fRepository.findByCpf(Util.formatarCpf(mDto.getCpf())) != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro","Cpf já em uso, verifique!"));
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
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro","Falha ao cadastrar usuário\n" + e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponseUtil.response("Sucesso", "Usuário cadastrado com sucesso"));
    };

    public ResponseEntity<?> login (String mEmail, String mSenha){
        try {
            String mEmailUpper = mEmail.toUpperCase();
            UsuarioVO mVO = fRepository.findByEmail(mEmailUpper);
            if (fRepository.findByEmail(mEmailUpper) == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ApiResponseUtil.response("Erro","E-mail ou senha incorreto!"));
            }

            var mAuthToken = new UsernamePasswordAuthenticationToken(mEmailUpper, mSenha);
            var mAuth = fAuthenticationManager.authenticate(mAuthToken);

            var mToken = fTokenService.generateToken((UsuarioVO) mAuth.getPrincipal());
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponseUtil.response("Sucesso","Login realizado com sucesso\n" + "Token: " + mToken));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponseUtil.response("Erro",  e.getMessage()));
        }
    }

    public ResponseEntity<?>BuscarPorUsuarioToken(String mToken){
        Optional<UsuarioTokenVO> mUsuarioTokenVO = fUsuarioTokenRepository.findByToken(mToken);
        if (mUsuarioTokenVO.isEmpty() || mUsuarioTokenVO == null){
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
                mUsuarioTokenVO.get().getUsuario().getId()
        );
    }

}
