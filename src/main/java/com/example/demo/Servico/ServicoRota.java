package com.example.demo.Servico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servico")
public class ServicoRota {
    @Autowired ServicoService fService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody @Valid ServicoDTO mDTO){
        return fService.cadastrar(mDTO);
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listar(){
        return fService.listar();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?>editar(@RequestBody @Valid ServicoDTO mDTO, @PathVariable Long id){
        return fService.editar(mDTO, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>deletar(@PathVariable Long id){
        return fService.excluir(id);
    }
}
