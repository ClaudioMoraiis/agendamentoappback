package com.example.demo.ProfissionalHorario;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/profissional-horario")
public class ProfissionalHorarioRota {
    @Autowired
    private ProfissionalHorarioService fService;

    @PostMapping("/register")
    private ResponseEntity<?>register(@Valid @RequestBody ProfissionalHorarioDTO mDTO){
        return fService.register(mDTO);
    }

    @GetMapping("/list")
    private List<Map<String, Object>> list(){
        return fService.list();
    }
}
