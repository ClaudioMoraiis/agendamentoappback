package com.example.demo.Usuario;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
public class UsuarioRota {
    @Autowired
    private UsuarioService fService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar (@Valid @RequestBody UsuarioCadastroDTO mDto){
        return fService.cadastrar(mDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(String mEmail, String mSenha){
        return fService.login(mEmail, mSenha);
    }

}
