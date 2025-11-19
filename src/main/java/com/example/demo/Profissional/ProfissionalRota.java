package com.example.demo.Profissional;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/profissional")
public class ProfissionalRota {
    @Autowired
    private ProfissionalService fService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?>cadastrar(@RequestBody @Valid ProfissionalDTO mDTO){
        return fService.cadastrar(mDTO);
    }

    @GetMapping("/listar")
    public List<Map<String, Object>> list(){
        return fService.list();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?>update(@PathVariable Long id, @RequestBody @Valid ProfissionalDTO mDTO){
        return fService.update(id, mDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>delete(@PathVariable Long id){
        return fService.delete(id);
    }

    @GetMapping("/{nome}")
    public ResponseEntity<?>idByNome(@PathVariable String nome){
        return fService.getIdByNome(nome);
    }
}


