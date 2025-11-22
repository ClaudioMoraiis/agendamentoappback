package com.example.demo.Agendamento;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agendamento")
public class AgendamentoRota {
    @Autowired
    private AgendamentoService fService;

    @PostMapping("/register")
    public ResponseEntity<?>register(@RequestBody @Valid AgendamentoDTO mDTO){
        return fService.register(mDTO);
    }

    @GetMapping("/listar")
    public List<? extends Object> list(){
        return fService.list();
    }

    @GetMapping("/horarios-disponiveis")
    public ResponseEntity<?> horariosDisponiveis(@RequestParam Long profissionalId, @RequestParam Long servicoId,
                                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate data){
        return fService.getAvailableTime(profissionalId, servicoId, data);
    }
}
