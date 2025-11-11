package com.example.demo.Especialidade;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class EspecialidadeDTO {
    @JsonProperty("nome")
    @NotNull(message = "Campo 'nome' não informado no body")
    private String nome;

    public EspecialidadeDTO(String nome) {
        this.nome = nome;
    }

    public EspecialidadeDTO(){}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EspecialidadeDTO that = (EspecialidadeDTO) o;
        return Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nome);
    }

    @Override
    public String toString() {
        return "EspecialidadeDTO{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
