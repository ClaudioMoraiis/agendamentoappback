package com.example.demo.Profissional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profissional")
public class ProfissionalRota {
    @Autowired
    private ProfissionalService fService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?>cadastrar(ProfissionalDTO mDTO){
        return fService.cadastrar(mDTO);
    }
}


