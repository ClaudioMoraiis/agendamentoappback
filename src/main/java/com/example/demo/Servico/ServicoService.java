package com.example.demo.Servico;

import com.example.demo.Util.ApiResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ServicoService {
    @Autowired ServicoRepository fRepository;

    public ServicoVO converterDtoParaVO(ServicoDTO mDTO){
        ServicoVO mServicoVO = new ServicoVO();
        mServicoVO.setNome(mDTO.getNome());
        mServicoVO.setDuracao(mDTO.getDuracao());
        mServicoVO.setValor(mDTO.getPreco());
        return mServicoVO;
    }

    public ResponseEntity<?> cadastrar(ServicoDTO mDTO){
        if (fRepository.getServico(mDTO.getNome(), mDTO.getPreco()) != null){
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
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", e.getMessage())
            );
        }
    }
}
