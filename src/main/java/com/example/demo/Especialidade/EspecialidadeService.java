package com.example.demo.Especialidade;

import com.example.demo.Util.ApiResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadeService {
    @Autowired
    private EspecialidadeRepository fRepository;

    public ResponseEntity<?>cadastrar(EspecialidadeDTO mDTO){
        EspecialidadeVO mEspecialidadeVO = new EspecialidadeVO();
        if (fRepository.findByNome(mDTO.getNome()) != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    ApiResponseUtil.response("Erro", "Já existe especialidade cadastrada com esse nome")
            );
        }

        mEspecialidadeVO.setNome(mDTO.getNome());
        try {
            fRepository.save(mEspecialidadeVO);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponseUtil.response("Sucesso", "Especialidade cadastrada com sucesso")
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", e.getMessage())
            );
        }
    }

    public List<EspecialidadeVO>list(){
        return fRepository.findAll();
    }
}
