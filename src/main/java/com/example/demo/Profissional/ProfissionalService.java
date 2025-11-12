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

    public ProfissionalVO converterDtoParaVO(ProfissionalDTO mDTO){
        ProfissionalVO mProfissionalVO = new ProfissionalVO();
        mProfissionalVO.setCelular(Util.formatarTelefone(mDTO.getTelefone()));
        mProfissionalVO.setEmail(mDTO.getEmail());
        mProfissionalVO.setStatus(mDTO.getStatus());
        mProfissionalVO.setNome(mDTO.getNome());

        return mProfissionalVO;
    }

    public ResponseEntity<?>validar(ProfissionalDTO mDTO, EspecialidadeVO mEspecialidadeVO){
        if (fRepository.findByCelular(Util.formatarTelefone(mDTO.getTelefone())) != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    ApiResponseUtil.response("Erro", "Já existe profissional com esse número de celular!")
            );
        }

        if (fRepository.findByEmail(mDTO.getEmail()) != null){
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

        if (!mDTO.getStatus().toUpperCase().matches(".*(TRUE|FALSE).*")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "No campo status informe TRUE ou FALSE!")
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
            ProfissionalVO mProfissionalVO = converterDtoParaVO(mDTO);
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
                    map.put("celular", mVO.getCelular());
                    map.put("id", mVO.getId());
                    map.put("especialidade", mVO.getEspecialidadeVO().getNome());
                    map.put("status", mVO.getStatus());
                    return map;
                })
                .toList();
    }
}
