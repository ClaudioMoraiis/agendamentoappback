package com.example.demo.EmailService;

import com.example.demo.Usuario.UsuarioRepository;
import com.example.demo.Usuario.UsuarioVO;
import com.example.demo.UsuarioToken.UsuarioTokenRepository;
import com.example.demo.UsuarioToken.UsuarioTokenVO;
import com.example.demo.Util.ApiResponseUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {
    @Autowired
    private UsuarioRepository fRepository;

    @Autowired
    private UsuarioTokenRepository fUsuarioTokenRepository;

    @Autowired
    private PasswordEncoder fPasswordEncoder;

    @Autowired
    private final JavaMailSender fMailSender;

    public EmailService(JavaMailSender fMailSender) {
        this.fMailSender = fMailSender;
    }


    public ResponseEntity<?> enviarEmailRecuperacaoSenha(String mPara) throws MessagingException {
        Optional<UsuarioVO> mUsuarioVO = Optional.ofNullable(fRepository.findByEmail(mPara));

        if (mUsuarioVO.isEmpty()) {
            return ResponseEntity.ok(
                    "Se este e-mail estiver cadastrado, você receberá um e-mail com instruções para redefinir sua senha.");
        }

        try {
            UsuarioTokenVO mUsuarioTokenVO = new UsuarioTokenVO(mUsuarioVO.get());
            String novoToken = UUID.randomUUID().toString();
            mUsuarioTokenVO.setUto_token(novoToken);
            mUsuarioTokenVO.setUto_dthr_expiracao(LocalDateTime.now().plusHours(1));
            mUsuarioTokenVO.setUto_ativo(true);
            fUsuarioTokenRepository.save(mUsuarioTokenVO);

            String token = mUsuarioTokenVO.getUto_token();

            String link = "http://localhost:5173/recuperar-senha?token=" + token;
            String assunto = "Recuperação senha";
            String conteudo = "<p>Clique no link abaixo para redefinir sua senha:</p>" + "<p><a href=\"" + link
                    + "\">Redefinir Senha</a></p>";

            MimeMessage mMensagem = fMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mMensagem, true);
            helper.setTo(mPara);
            helper.setSubject(assunto);
            helper.setText(conteudo, true);

            fMailSender.send(mMensagem);

            return ResponseEntity.ok("E-mail enviado");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar solicitação: " + e.getMessage());
        }
    }

    public ResponseEntity<?> redefinirSenha(Map<String, String> request) {
        String token = request.get("token");
        String novaSenha = request.get("senha");
        String confirmarSenha = request.get("confirmarSenha");

        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token ausente");
        }

        if (novaSenha == null || novaSenha.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "Senha não informada no body")
            );
        }

        if (confirmarSenha == null || confirmarSenha.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "Senha não informada no body")
            );
        }


        Optional<UsuarioTokenVO> usuarioToken = fUsuarioTokenRepository.findByToken(token);
        if (usuarioToken.isEmpty() || usuarioToken.get().getUto_dthr_expiracao().isBefore(LocalDateTime.now()) ||
                usuarioToken.get().get_uto_ativo().equals(false)) {
            return ResponseEntity.badRequest().body("Token inválido ou expirado");
        }

        if (!novaSenha.equals(confirmarSenha)) {
            return ResponseEntity.badRequest().body("As senhas não coincidem");
        }

        UsuarioVO usuario = usuarioToken.get().getUsuario();
        String senhaCriptografada = fPasswordEncoder.encode(novaSenha);
        usuario.setSenha(senhaCriptografada);
        usuarioToken.get().setUto_ativo(false);

        fRepository.save(usuario);
        return ResponseEntity.ok("Senha redefinida com sucesso!");
    }
}
