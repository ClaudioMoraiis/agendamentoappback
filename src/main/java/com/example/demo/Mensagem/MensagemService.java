package com.example.demo.Mensagem;

import com.example.demo.Enum.MessageStatusEnum;
import com.example.demo.Usuario.UsuarioRepository;
import com.example.demo.Usuario.UsuarioVO;
import com.example.demo.Util.ApiResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class MensagemService {
    @Autowired
    private UsuarioRepository fUsuarioRepository;

    @Autowired
    private MensagemRepository fRepository;

    @Autowired
    private SimpMessagingTemplate fSimpMessagingTemplate;

    public ResponseEntity<?> cadastrar(MensagemDTO mDTO) {
        Optional<UsuarioVO> mUsuarioSenderVO = fUsuarioRepository.findById(mDTO.getSenderId());
        Optional<UsuarioVO> mUsuarioDestinoVO = fUsuarioRepository.findById(mDTO.getClienteId());

        if (mUsuarioSenderVO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Cliente destino não localizado com esse id")
            );
        } else if (mUsuarioDestinoVO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Cliente não localizado com esse id")
            );
        }

        MensagemVO mMensagemVO = new MensagemVO();
        mMensagemVO.setConteudo(mDTO.getConteudo());
        mMensagemVO.setCreatedAt(LocalDateTime.now());
        mMensagemVO.setStatus(MessageStatusEnum.SENT);
        mMensagemVO.setClient(mUsuarioDestinoVO.get());
        mMensagemVO.setSender(mUsuarioSenderVO.get());
        try {
            fRepository.save(mMensagemVO);
            fSimpMessagingTemplate.convertAndSendToUser(
                    mUsuarioDestinoVO.get().getId().toString(),
                    "/queue/messages",
                    mDTO
            );

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponseUtil.response("Sucesso", "Mensagem cadastrada com sucesso!")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", e.getMessage())
            );
        }
    }

    public ResponseEntity<?> marcarComoLida(Long mId, Long mIdUsuarioLeitor) {

        Optional<MensagemVO> mMensagemVO = fRepository.findById(mId);
        if (mMensagemVO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Mensagem não localizada!")
            );
        }

        MensagemVO mensagem = mMensagemVO.get();

        // Valida se quem está lendo é o destinatário
        Long mIdDestinatarioMensagem = mensagem.getClient().getId();
        if (!mIdUsuarioLeitor.equals(mIdDestinatarioMensagem)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    ApiResponseUtil.response("Erro", "Usuário não pode marcar essa mensagem como lida")
            );
        }

        // Evita salvar duas vezes
        if (mensagem.getStatus() == MessageStatusEnum.READ) {
            return ResponseEntity.ok(
                    ApiResponseUtil.response("Sucesso", "Mensagem já estava como lida")
            );
        }

        // Marca como READ
        mensagem.setStatus(MessageStatusEnum.READ);
        fRepository.save(mensagem);

        // Notifica o REMETENTE
        Long mIdRemetente = mensagem.getSender().getId();

        Map<String, Object> mPayload = new HashMap<>();
        mPayload.put("mensagemId", mensagem.getId());
        mPayload.put("status", MessageStatusEnum.READ.name());

        fSimpMessagingTemplate.convertAndSendToUser(
                mIdRemetente.toString(),
                "/queue/message-status",
                mPayload
        );

        return ResponseEntity.ok(
                ApiResponseUtil.response("Sucesso", "Mensagem alterada para lida com sucesso!")
        );
    }

}
