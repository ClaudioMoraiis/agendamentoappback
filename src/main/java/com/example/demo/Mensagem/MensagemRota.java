package com.example.demo.Mensagem;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mensagem")
public class MensagemRota {
     @Autowired
     private MensagemService fService;

    @PostMapping("/cadastrar")
    private ResponseEntity<?>cadastrar(@RequestBody @Valid MensagemDTO mDTO){
        return fService.cadastrar(mDTO);
    }

    @PatchMapping("/{id}/{idUsuarioDestino}/lida")
    private ResponseEntity<?>marcarComoLida(@PathVariable Long id, @PathVariable Long idUsuarioDestino){
        return fService.marcarComoLida(id, idUsuarioDestino);
    }

}
