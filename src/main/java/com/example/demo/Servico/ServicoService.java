package com.example.demo.Servico;

import com.example.demo.Util.ApiResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ServicoService {
    @Autowired
    ServicoRepository fRepository;

    public ServicoVO converterDtoParaVO(ServicoDTO mDTO) {
        ServicoVO mServicoVO = new ServicoVO();
        mServicoVO.setNome(mDTO.getNome());
        mServicoVO.setDuracao(mDTO.getDuracao());
        mServicoVO.setValor(mDTO.getPreco());
        return mServicoVO;
    }

    public ResponseEntity<?> cadastrar(ServicoDTO mDTO) {
        if (fRepository.getServico(mDTO.getNome(), mDTO.getPreco()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    ApiResponseUtil.response("Erro", "Já existe serviço com mesmo nome e preço cadastrado!")
            );
        }

        try {
            ServicoVO mServicoVO = converterDtoParaVO(mDTO);
            fRepository.save(mServicoVO);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponseUtil.response("Sucesso", "Serviço cadastrado com sucesso!")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", e.getMessage())
            );
        }
    }

    public ResponseEntity<?> listar() {
        return ResponseEntity.status(HttpStatus.OK).body(fRepository.findAll());
    }

    public ResponseEntity<?> editar(ServicoDTO mDTO, Long mId) {
        Optional<ServicoVO> mServicoVO = fRepository.findById(mId);
        if (mServicoVO.isEmpty() || mServicoVO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response(
                            "Erro", "Nenhum serviço localizado com esse ID!"
                    )
            );
        }

        if (fRepository.getServico(mDTO.getNome(), mDTO.getPreco()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    ApiResponseUtil.response("Erro", "Já existe serviço com mesmo nome e preço cadastrado!")
            );
        }

        mServicoVO.get().setValor(mDTO.getPreco());
        mServicoVO.get().setDuracao(mDTO.getDuracao());
        mServicoVO.get().setNome(mDTO.getNome());
        try {
            fRepository.save(mServicoVO.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponseUtil.response(
                            "Sucesso", "Serviço editado com sucesso!"
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response(
                            "Erro", e.getMessage()
                    )
            );
        }
    }

    public ResponseEntity<?> excluir(Long mId) {
        Optional<ServicoVO> mServicoVO = fRepository.findById(mId);
        if (mServicoVO.isEmpty() || mServicoVO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response(
                            "Erro", "Nenhum serviço localizado com esse ID!"
                    )
            );
        }

        try {
            fRepository.delete(mServicoVO.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponseUtil.response(
                            "Erro", "Serviço excluido com sucesso!"
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response(
                            "Erro", e.getMessage()
                    )
            );
        }
    }

    //Todo: Criar um método para validar as requisições, está ficando muita validação nas rotas
}
