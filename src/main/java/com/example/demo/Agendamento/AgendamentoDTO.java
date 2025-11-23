package com.example.demo.Agendamento;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class AgendamentoDTO {
    @JsonProperty("usuarioId")
    @NotNull(message = "Campo 'usuarioId' não informado no body, verifique!")
    private Long usuarioId;

    @JsonProperty("servicoId")
    @NotNull(message = "Campo 'servicoId' não informado no body, verifique!")
    private Long servicoId;

    @JsonProperty("profissionalId")
    @NotNull(message = "Campo 'profissionalId' não informado no body, verifique!")
    private Long profissionalId;

    @JsonProperty("valor")
    @NotNull(message = "Campo 'valor' não informado no body, verifique!")
    private BigDecimal valor;

    @JsonProperty("data")
    @NotNull(message = "Campo 'data' não informado no body, verifique!")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;

    @JsonProperty("horario")
    @NotNull(message = "Campo 'horario' não informado no body, verifique!")
    @JsonFormat(pattern = "HH:mm")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horario;

    @JsonProperty("status")
    @NotNull(message = "Campo 'status' não informado no body, verifique!")
    @Enumerated(EnumType.STRING)
    private EnumAgendamentoStatus status;

    @JsonProperty("usuarioCadastrado")
    @NotNull(message = "Campo 'usuarioCadastrado' não informado no body, verifique!")
    private String usuarioCadastrado;

    @JsonProperty("nomeUsuario")
    private String nomeUsuario;

    public AgendamentoDTO(Long usuarioId, Long servicoId, Long profissionalId, BigDecimal valor, LocalDate data,
                          LocalTime horario, EnumAgendamentoStatus status, String usuarioCadastrado, String nomeUsuario) {
        this.usuarioId = usuarioId;
        this.servicoId = servicoId;
        this.profissionalId = profissionalId;
        this.valor = valor;
        this.data = data;
        this.horario = horario;
        this.status = status;
        this.usuarioCadastrado = usuarioCadastrado;
        this.nomeUsuario = nomeUsuario;
    }

    public AgendamentoDTO(){}

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public Long getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(Long profissionalId) {
        this.profissionalId = profissionalId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public EnumAgendamentoStatus getStatus() {
        return status;
    }

    public void setStatus(EnumAgendamentoStatus status) {
        this.status = status;
    }

    public String getUsuarioCadastrado() {
        return usuarioCadastrado;
    }

    public void setUsuarioCadastrado(String usuarioCadastrado) {
        this.usuarioCadastrado = usuarioCadastrado;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof AgendamentoDTO that)) return false;

        return Objects.equals(usuarioId, that.usuarioId) && Objects.equals(servicoId, that.servicoId) &&
                Objects.equals(profissionalId, that.profissionalId) && Objects.equals(valor, that.valor) &&
                Objects.equals(data, that.data) && Objects.equals(horario, that.horario) && status == that.status &&
                Objects.equals(usuarioCadastrado, that.usuarioCadastrado) && Objects.equals(nomeUsuario, that.nomeUsuario);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(usuarioId);
        result = 31 * result + Objects.hashCode(servicoId);
        result = 31 * result + Objects.hashCode(profissionalId);
        result = 31 * result + Objects.hashCode(valor);
        result = 31 * result + Objects.hashCode(data);
        result = 31 * result + Objects.hashCode(horario);
        result = 31 * result + Objects.hashCode(status);
        result = 31 * result + Objects.hashCode(usuarioCadastrado);
        result = 31 * result + Objects.hashCode(nomeUsuario);
        return result;
    }

    @Override
    public String toString() {
        return "AgendamentoDTO{" +
                "usuarioId=" + usuarioId +
                ", servicoId=" + servicoId +
                ", profissionalId='" + profissionalId + '\'' +
                ", valor=" + valor +
                ", data=" + data +
                ", horario=" + horario +
                ", status=" + status +
                ", usuarioCadastrado=" + usuarioCadastrado +
                ", nomeUsuario=" + nomeUsuario +
                '}';
    }
}
