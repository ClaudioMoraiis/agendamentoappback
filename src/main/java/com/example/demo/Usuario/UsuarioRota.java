package com.example.demo.Usuario;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String senha){
        return fService.login(email, senha);
    }

}
