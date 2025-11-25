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
    @NotNull(message = "Campo 'telefone' não informado no body")
    private String telefone;

    @JsonProperty("especialidade")
    @NotNull(message = "Campo 'especialidade' não informado no body")
    private String especialidade;

    @JsonProperty("ativo")
    @NotNull(message = "Campo 'ativo' não informado no body")
    private String ativo;

    @JsonProperty("email")
    @Email
    @NotNull(message = "Campo 'email' não informado no body")
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    public ProfissionalDTO(String nome, String telefone, String especialidade, String ativo, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.especialidade = especialidade;
        this.ativo = ativo;
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

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String status) {
        this.ativo = status;
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
               Objects.equals(especialidade, that.especialidade) && Objects.equals(ativo, that.ativo) &&
               Objects.equals(email, that.email) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, telefone, especialidade, ativo, email);
    }

    @Override
    public String toString() {
        return "ProfissionalDTO{" +
                "nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                ", especialidade='" + especialidade + '\'' +
                ", ativo='" + ativo + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
