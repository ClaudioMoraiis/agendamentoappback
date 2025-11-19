package com.example.demo.Agendamento;

import com.example.demo.Profissional.ProfissionalRepository;
import com.example.demo.Servico.ServicoRepository;
import com.example.demo.Usuario.UsuarioRepository;
import com.example.demo.UsuarioToken.UsuarioTokenRepository;
import com.example.demo.Util.ApiResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AgendamentoService {
    @Autowired
    private AgendamentoRepository fRepository;

    @Autowired
    private ProfissionalRepository fProfissionalRepository;

    @Autowired
    private ServicoRepository fServicoRepository;

    @Autowired
    private UsuarioRepository fUsuarioRepository;

    public ResponseEntity<?>isValid(AgendamentoDTO mDTO){
        if (fProfissionalRepository.findById(mDTO.getProfissionalId()) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Nenhum profissional localizado com esse id!")
            );
        }

        if (fServicoRepository.findById(mDTO.getServicoId()) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Nenhum serviço localizado com esse id!")
            );
        }

        if (fUsuarioRepository.findById(mDTO.getUsuarioId()) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Nenhum usuário localizado com esse id!")
            );
        }

        if (!mDTO.getStatus().toString().toUpperCase().matches("CONFIRMADO|CANCELADO|PENDENTE|CONCLUIDO")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "No campo stattus informe entre CONFIRMADO|CANCELADO|PENDENTE|CONCLUIDO!")
            );
        }

        return null;
    }

    public ResponseEntity<?>register(AgendamentoDTO mDTO){
        ResponseEntity<?>mValidacao = isValid(mDTO);
        if (mValidacao != null){
            return mValidacao;
        }

        //TODO ver se a validação está certa, se falta mais algo etc...

        return null;
    }
}
