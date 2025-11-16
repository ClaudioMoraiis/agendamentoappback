package com.example.demo.ProfissionalHorario;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class ProfissionalHorarioDTO {
    @JsonProperty("profissionalId")
    @NotNull(message = "Campo 'profissionalId' não informado no body!")
    private Long profissionalId;

    @NotEmpty
    private List<
                @Pattern(regexp = "seg|ter|qua|qui|sex|sab|dom",
                        message = "Dia inválido. Valores permitidos: seg, ter, qua, qui, sex, sab, dom")
                        String
                > diaSemana;

    @JsonProperty("horaInicio")
    @JsonFormat(pattern = "HH:mm")
    @NotNull(message = "Campo 'horaInicio' não informado no body!")
    private LocalTime horaInicio;

    @JsonProperty("horaFinal")
    @JsonFormat(pattern = "HH:mm")
    @NotNull(message = "Campo 'horaFinal' não informado no body!")
    private LocalTime horaFinal;

    public ProfissionalHorarioDTO(Long profissionalId, List<@Pattern(regexp = "seg|ter|qua|qui|sex|sab|dom",
            message = "Dia inválido. Valores permitidos: seg, ter, qua, qui, sex, sab, dom") String> diaSemana, LocalTime horaInicio, LocalTime horaFinal) {
        this.profissionalId = profissionalId;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFinal = horaFinal;
    }

    public ProfissionalHorarioDTO (){}

    public Long getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(Long profissionalId) {
        this.profissionalId = profissionalId;
    }

    public List<

            String
            > getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(List<

            String
            > diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(LocalTime horaFinal) {
        this.horaFinal = horaFinal;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ProfissionalHorarioDTO that)) return false;

        return Objects.equals(profissionalId, that.profissionalId) && Objects.equals(diaSemana, that.diaSemana) && Objects.equals(horaInicio, that.horaInicio) && Objects.equals(horaFinal, that.horaFinal);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(profissionalId);
        result = 31 * result + Objects.hashCode(diaSemana);
        result = 31 * result + Objects.hashCode(horaInicio);
        result = 31 * result + Objects.hashCode(horaFinal);
        return result;
    }

    @Override
    public String toString() {
        return "ProfissionalHorarioDTO{" +
                "profissionalId=" + profissionalId +
                ", diaSemana=" + diaSemana +
                ", horaInicio=" + horaInicio +
                ", horaFinal=" + horaFinal +
                '}';
    }
}
