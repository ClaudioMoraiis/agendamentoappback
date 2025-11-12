package com.example.demo.Profissional;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat;

import java.util.Objects;

public class ProfissionalDTO {
    @JsonProperty("nome")
    @NotNull(message = "Campo 'nome' não informado no body")
    private String nome;

    @JsonProperty("telefone")
    @NotNull(message = "telefone 'email' não informado no body")
    private String telefone;

    @JsonProperty("especialidade")
    @NotNull(message = "Campo 'especialidade' não informado no body")
    private Long especialidade;

    @JsonProperty("status")
    @NotNull(message = "Campo 'status' não informado no body")
    private String status;

    @JsonProperty("email")
    @Email
    @NotNull(message = "Campo 'email' não informado no body")
    private String email;

    public ProfissionalDTO(String nome, String telefone, Long especialidade, String status, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.especialidade = especialidade;
        this.status = status;
        this.email = email;
    }

    public ProfissionalDTO(){}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Long getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Long especialidade) {
        this.especialidade = especialidade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProfissionalDTO that = (ProfissionalDTO) o;
        return Objects.equals(nome, that.nome) && Objects.equals(telefone, that.telefone) &&
               Objects.equals(especialidade, that.especialidade) && Objects.equals(status, that.status) &&
               Objects.equals(email, that.email) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, telefone, especialidade, status, email);
    }

    @Override
    public String toString() {
        return "ProfissionalDTO{" +
                "nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                ", especialidade='" + especialidade + '\'' +
                ", status='" + status + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
