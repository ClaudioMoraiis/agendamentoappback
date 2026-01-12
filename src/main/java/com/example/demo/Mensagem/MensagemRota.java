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

    @PutMapping("/lidas")
    private ResponseEntity<?>marcaComoLidaVarias(@RequestParam Long idCliente){
        return fService.marcarComoLidaVarias(idCliente);
    }

    @GetMapping("/mensagens")
    private ResponseEntity<?> listarMensagens(
            @RequestParam Long mClienteId,
            @RequestParam Long mUsuarioId,
            @RequestParam(defaultValue = "0") int mPage,
            @RequestParam(defaultValue = "20") int mSize
    ){
        return ResponseEntity.ok(fService.listarMensagens(mClienteId, mUsuarioId, mPage, mSize));
    }

    @DeleteMapping("/deletarVarias/{idCliente}/{idUsuarioLogado}")
    private ResponseEntity<?> deletarVarias(@PathVariable Long idCliente, @PathVariable Long idUsuarioLogado){
        return fService.deletarVarias(idCliente, idUsuarioLogado);
    }

    @DeleteMapping("/{idMensagem}/{usuarioId}")
    private ResponseEntity<?> deletar(@PathVariable Long idMensagem, @PathVariable Long usuarioId){
        return fService.deletar(idMensagem, usuarioId);
    }
}
