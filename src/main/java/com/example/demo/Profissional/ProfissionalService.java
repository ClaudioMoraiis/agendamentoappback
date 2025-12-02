package com.example.demo.Profissional;

import com.example.demo.Especialidade.EspecialidadeRepository;
import com.example.demo.Especialidade.EspecialidadeVO;
import com.example.demo.Usuario.UsuarioVO;
import com.example.demo.Util.ApiResponseUtil;
import com.example.demo.Util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProfissionalService {
    @Autowired
    private ProfissionalRepository fRepository;

    @Autowired
    private EspecialidadeRepository fEspecialidadeRepository;

    public ProfissionalVO converterDtoParaVO(ProfissionalDTO mDTO, ProfissionalVO mVO){
        mVO.setCelular(Util.formatarTelefone(mDTO.getTelefone()));
        mVO.setEmail(mDTO.getEmail());
        mVO.setAtivo(mDTO.getAtivo());
        mVO.setNome(mDTO.getNome());

        return mVO;
    }

    public ResponseEntity<?>validar(ProfissionalDTO mDTO, EspecialidadeVO mEspecialidadeVO){
        if (fRepository.existsByEmailAndIdNot(mDTO.getEmail(), mDTO.getId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    ApiResponseUtil.response("Erro", "Já existe profissional com esse e-mail!")
            );
        }

        if (mEspecialidadeVO == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Especialidade não existe, verifique!")
            );
        }

        if (!Util.validaTelefone(mDTO.getTelefone())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Telefone inválido!")
            );
        }

        if (fRepository.existsByCelularAndIdNot(Util.formatarTelefone(mDTO.getTelefone()), mDTO.getId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    ApiResponseUtil.response("Erro", "Já existe profissional com esse número de celular!")
            );
        }

        if (!mDTO.getAtivo().toUpperCase().matches(".*(TRUE|FALSE).*")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "No campo status informe TRUE ou FALSE!")
            );
        }

        boolean mExisteProfissional = (mDTO.getId() != null) ? fRepository.existsByNomeAndIdNot(mDTO.getNome(), mDTO.getId()) : fRepository.existsByNome(mDTO.getNome());
        if (mExisteProfissional){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    ApiResponseUtil.response("Erro", "Já existe profissional com esse nome!")
            );
        }

        if (((mDTO.getId() != null) &&
             (!fRepository.getAtivo(mDTO.getId()).equals(mDTO.getAtivo())) &&
             (fRepository.getAgendemento(mDTO.getId()) != null) &&
             (mDTO.getAtivo().toUpperCase().equals("FALSE")))){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Não é possível inativar com agendamento ativo ou pendente")
            );
        }

        return null;
    }

    public ResponseEntity<?>cadastrar(ProfissionalDTO mDTO){
        EspecialidadeVO mEspecialidadeVO = fEspecialidadeRepository.findByNome(mDTO.getEspecialidade());
        ResponseEntity<?>mValidacao = validar(mDTO, mEspecialidadeVO);
        if (mValidacao != null){
            return mValidacao;
        }

        try {
            ProfissionalVO mProfissionalVO = new ProfissionalVO();
            mProfissionalVO = converterDtoParaVO(mDTO, mProfissionalVO);
            mProfissionalVO.setEspecialidadeVO(mEspecialidadeVO);
            fRepository.save(mProfissionalVO);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponseUtil.response("Sucesso", "Funcionário cadastrado com sucesso!")
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", e.getMessage())
            );
        }
    }

    public List<Map<String, Object>> list() {
        List<ProfissionalVO> mProfissionalVO = fRepository.findAll();
        return mProfissionalVO.stream()
                .map(mVO -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("nome", mVO.getNome());
                    map.put("email", mVO.getEmail());
                    map.put("telefone", mVO.getCelular());
                    map.put("id", mVO.getId());
                    map.put("especialidade", mVO.getEspecialidadeVO().getNome());
                    map.put("ativo", mVO.getAtivo());
                    return map;
                })
                .toList();
    }

    public ResponseEntity<?>update(Long mId, ProfissionalDTO mDTO){
        Optional<ProfissionalVO> mProfissionalOpt = fRepository.findById(mId);
        if (mProfissionalOpt.isEmpty() || mProfissionalOpt == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Nenhum profissional localizado com esse id!")
            );
        }

        EspecialidadeVO mEspecialidadeVO = fEspecialidadeRepository.findByNome(mDTO.getEspecialidade());
        mDTO.setId(mId);

        ResponseEntity<?>mValidacao = validar(mDTO, mEspecialidadeVO);
        if (mValidacao != null){
            return mValidacao;
        }

        try {
            ProfissionalVO mProfissionalVO = converterDtoParaVO(mDTO, mProfissionalOpt.get());
            mProfissionalVO.setEspecialidadeVO(mEspecialidadeVO);
            fRepository.save(mProfissionalVO);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponseUtil.response("Sucesso", "Registro alterado com sucesso")
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", e.getMessage())
            );
        }

    }

    public ResponseEntity<?>delete(Long mId){
        Optional<ProfissionalVO> mProfissionalVO = fRepository.findById(mId);
        if (mProfissionalVO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Não existe profissional com esse id!")
            );
        }

        try {
            fRepository.delete(mProfissionalVO.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponseUtil.response("Sucesso", "Registro excluido com sucesso!")
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Não existe profissional com esse id!")
            );
        }
    }

    public ResponseEntity<?>getIdByNome(String mNome){
        ProfissionalVO mProfissionalVO = fRepository.findByNome(mNome.toUpperCase());
        if (mProfissionalVO == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Não existe profissional com esse nome!")
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponseUtil.response("Sucesso", mProfissionalVO.getId().toString())
        );
    }
}
