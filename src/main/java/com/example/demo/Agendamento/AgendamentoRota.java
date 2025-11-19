package com.example.demo.Agendamento;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agendamento")
public class AgendamentoRota {
    @Autowired
    private AgendamentoService fService;

    @PostMapping("/register")
    public ResponseEntity<?>register(@RequestBody @Valid AgendamentoDTO mDTO){
        return fService.register(mDTO);
    }
}
