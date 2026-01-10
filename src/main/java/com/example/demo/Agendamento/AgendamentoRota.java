package com.example.demo.Agendamento;

import com.example.demo.Jwt.SecurityConfigurations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agendamento")
@Tag(name = "AgendamentoAPP", description = "Agendamento de serviços")
//@SecurityRequirement(name = SecurityConfigurations.SECURITY)
public class AgendamentoRota {
    @Autowired
    private AgendamentoService fService;

    @PostMapping("/register")
    @Operation(summary = "Cadastra agendamento")
    @ApiResponse(responseCode = "202", description = "Agendamento cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro ao cadastrar")
    public ResponseEntity<?>register(@RequestBody @Valid AgendamentoDTO mDTO){
        return fService.register(mDTO);
    }

    @GetMapping("/listar")
    @Operation(summary = "Lista os agendamentos")
    public List<? extends Object> list(){
        return fService.list();
    }

    @GetMapping("/horarios-disponiveis")
    @Operation(summary = "Lista os horários disponiveis por profissional")
    @ApiResponse(responseCode = "200", description = "Agendamentos listados")
    @ApiResponse(responseCode = "404", description = "Nenhum horário disponível para esse profissional")
    public ResponseEntity<?> horariosDisponiveis(@RequestParam Long profissionalId, @RequestParam Long servicoId,
                                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate data){
        return fService.getAvailableTimeProfissional(profissionalId, servicoId, data);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Altera o agendamento")
    @ApiResponse(responseCode = "200", description = "Agendamento alterado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro ao alterar")
    public ResponseEntity<?> edit(@RequestBody @Valid AgendamentoDTO mDTO, @PathVariable Long id){
        return fService.edit(mDTO, id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta agendamento")
    @ApiResponse(responseCode = "200", description = "Agendamento deletado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro ao deletar")
    @ApiResponse(responseCode = "404", description = "Nenhum agendamento localizado com esse id")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return fService.delete(id);
    }

    @GetMapping("list/{clientId}")
    @Operation(summary = "Lista os agendamentos por cliente")
    @ApiResponse(responseCode = "200", description = "Listado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro ao listar")
    public List<? extends Object> listPerClient(@PathVariable Long clientId){
        return fService.listPerClient(clientId);
    }

    @PutMapping("/cancel/{id}")
    @Operation(summary = "Cancela o agendamento")
    @ApiResponse(responseCode = "200", description = "Cancelado com sucesso")
    @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    public ResponseEntity<?> cancel(@PathVariable Long id){
        return fService.cancel(id);
    }
}
