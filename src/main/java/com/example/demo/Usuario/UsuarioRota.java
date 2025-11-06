package com.example.demo.Usuario;

import com.example.demo.EmailService.EmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/usuario")
public class UsuarioRota {
    @Autowired
    private UsuarioService fService;

    @Autowired
    private EmailService fEmailService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar (@Valid @RequestBody UsuarioCadastroDTO mDto){
        return fService.cadastrar(mDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String senha){
        return fService.login(email, senha);
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<?>recuperarSenha(@RequestParam String email) throws MessagingException {
        return fEmailService.enviarEmailRecuperacaoSenha(email);
    }

    @PutMapping("/redefinir-senha")
    public ResponseEntity<?> redefinirSenha(@RequestBody Map<String, String> mRequest) {
        return fEmailService.redefinirSenha(mRequest);
    }

    @GetMapping("por-usuario-token/{token}")
    public ResponseEntity<?>porUsuarioToken(@PathVariable String token){
        return fService.BuscarPorUsuarioToken(token);
    }
}
