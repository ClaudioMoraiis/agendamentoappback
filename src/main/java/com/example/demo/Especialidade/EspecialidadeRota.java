package com.example.demo.Especialidade;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/especialidade")
public class EspecialidadeRota {
    @Autowired
    private EspecialidadeService fService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?>cadastrar(@RequestBody @Valid EspecialidadeDTO mDTO){
        return fService.cadastrar(mDTO);
    }

    @GetMapping("/listar")
    public List<EspecialidadeVO> list(){
        return fService.list();
    }
}
