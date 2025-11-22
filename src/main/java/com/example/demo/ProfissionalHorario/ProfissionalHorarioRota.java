package com.example.demo.ProfissionalHorario;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/profissional-horario")
public class ProfissionalHorarioRota {
    @Autowired
    private ProfissionalHorarioService fService;

    @PostMapping("/register")
    private ResponseEntity<?>register(@Valid @RequestBody ProfissionalHorarioDTO mDTO){
        return fService.register(mDTO);
    }

}
