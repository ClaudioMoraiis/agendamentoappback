package com.example.demo.Mensagem;

import com.example.demo.Enum.MessageStatusEnum;
import com.example.demo.Usuario.UsuarioRepository;
import com.example.demo.Usuario.UsuarioVO;
import com.example.demo.Util.ApiResponseUtil;
import com.example.demo.Util.PageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
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
                    ApiResponseUtil.response("Erro", "Remetente não localizado com esse id")
            );
        } else if (mUsuarioDestinoVO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Destinatário não localizado com esse id")
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

            String emailDestino = mUsuarioDestinoVO.get().getEmail();
            String emailRemetente = mUsuarioSenderVO.get().getEmail();

            System.out.println("🔔 Enviando WebSocket para DESTINO: " + emailDestino);
            fSimpMessagingTemplate.convertAndSendToUser(
                    emailDestino,
                    "/queue/messages",
                    mDTO
            );
            System.out.println("✅ Enviado para destino!");

            System.out.println("🔔 Enviando WebSocket para REMETENTE: " + emailRemetente);
            fSimpMessagingTemplate.convertAndSendToUser(
                    emailRemetente,
                    "/queue/messages",
                    mDTO
            );
            System.out.println("✅ Enviado para remetente!");

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponseUtil.response("Sucesso", "Mensagem cadastrada com sucesso!")
            );
        } catch (Exception e) {
            System.err.println("❌ ERRO: " + e.getMessage());
            e.printStackTrace();
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
                mensagem.getSender().getEmail(),
                "/queue/message-status",
                mPayload
        );

        return ResponseEntity.ok(
                ApiResponseUtil.response("Sucesso", "Mensagem alterada para lida com sucesso!")
        );
    }

    public ResponseEntity<?> marcarComoLidaVarias(Long mIdCliente) {
        Optional<UsuarioVO> mUsuarioVO = fUsuarioRepository.findById(mIdCliente);
        if (mUsuarioVO.isEmpty()) {
            return ResponseEntity.ok(
                    ApiResponseUtil.response("Erro", "Nenhum cliente localizado com esse ID!")
            );
        }

        try {
            fRepository.marcarComoLida(mIdCliente);
            fSimpMessagingTemplate.convertAndSendToUser(
                    mUsuarioVO.get().getEmail(), // ou todos envolvidos
                    "/queue/messages-read",
                    Map.of("clienteId", mIdCliente)
            );


            return ResponseEntity.ok(
                    ApiResponseUtil.response("Erro", "Mensagens marcadas como lida com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ApiResponseUtil.response("Erro", e.getMessage())
            );
        }
    }

    public PageDTO<MensagemDTO> listarMensagens(Long clientId, Long mUsuarioId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());

        // ✅ USA O MÉTODO CORRETO QUE FILTRA MENSAGENS DELETADAS
        Page<MensagemVO> mensagens = fRepository.buscarConversacaoComFiltro(clientId, mUsuarioId, pageable);

        List<MensagemDTO> dtoList = mensagens.stream().map(m -> {
            return new MensagemDTO(
                    m.getClient().getId(),
                    m.getSender().getId(),
                    m.getConteudo(),
                    m.getStatus().toString()
            );
        }).toList();

        return new PageDTO<>(
                dtoList,
                mensagens.getTotalElements(),
                mensagens.getTotalPages(),
                mensagens.getNumber(),
                mensagens.getSize()
        );
    }

    public ResponseEntity<?> deletarVarias(Long clienteId, Long usuarioLogadoId) {
        if (!fUsuarioRepository.existsById(clienteId)) {
            return ResponseEntity.badRequest().body(
                    ApiResponseUtil.response("Erro", "Cliente não encontrado")
            );
        }

        List<MensagemVO> mensagens = fRepository.buscarTodasMensagensDaConversa(clienteId, usuarioLogadoId);
        for (MensagemVO mensagem : mensagens) {
            if (mensagem.getSender().getId().equals(usuarioLogadoId)) {
                mensagem.setDeletedBySender(true);
            }

            if (mensagem.getClient().getId().equals(usuarioLogadoId)) {
                mensagem.setDeletedByRecipient(true);
            }

            fSimpMessagingTemplate.convertAndSendToUser(
                    mensagem.getSender().getEmail(),
                    "/queue/conversation-deleted",
                    clienteId
            );

            fSimpMessagingTemplate.convertAndSendToUser(
                    mensagem.getClient().getEmail(),
                    "/queue/conversation-deleted",
                    clienteId
            );
        }

        fRepository.saveAll(mensagens);
        return ResponseEntity.ok(
                ApiResponseUtil.response("Sucesso", "Conversa apagada com sucesso")
        );
    }

    public ResponseEntity<?> deletar(Long mensagemId, Long usuarioId) {
        MensagemVO mensagem = fRepository.findById(mensagemId)
                .orElseThrow(() -> new RuntimeException("Mensagem não encontrada"));

        if (!mensagem.getSender().getId().equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponseUtil.response("Erro", "Ação não permitida"));
        }

        if (mensagem.getStatus() == MessageStatusEnum.READ) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseUtil.response("Erro", "Mensagem já foi visualizada"));
        }

        mensagem.setDeletedBySender(true);
        mensagem.setDeletedByRecipient(true);

        fRepository.save(mensagem);

        fSimpMessagingTemplate.convertAndSendToUser(
                mensagem.getSender().getEmail(),
                "/queue/message-deleted",
                mensagem.getId()
        );

        fSimpMessagingTemplate.convertAndSendToUser(
                mensagem.getClient().getEmail(),
                "/queue/message-deleted",
                mensagem.getId()
        );

        return ResponseEntity.ok(
                ApiResponseUtil.response("Sucesso", "Mensagem apagada para todos")
        );
    }

    public ResponseEntity<?> contarNaoLidas(Long usuarioId) {
        if (usuarioId == null || !fUsuarioRepository.existsById(usuarioId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "Usuário não informado ou inválido")
            );
        }
        long total = fRepository.contarNaoLidasParaUsuario(usuarioId);
        return ResponseEntity.ok(Map.of("total", total));
    }

}
