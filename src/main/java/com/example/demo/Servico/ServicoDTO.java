package com.example.demo.Servico;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ServicoDTO {
    @JsonProperty("nome")
    @NotNull(message = "Informe o campo 'nome no body'")
    private String nome;

    @JsonProperty("duracao")
    @NotNull(message = "Informe o campo 'duracao' no body")
    private String duracao;

    @DecimalMin("0.0")
    @JsonProperty("preco")
    @NotNull(message = "Informe o campo 'preco' no body")
    private BigDecimal preco;

    public ServicoDTO(String nome, String email, BigDecimal preco) {
        this.nome = nome;
        this.duracao = email;
        this.preco = preco;
    }

    public ServicoDTO(){}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ServicoDTO that)) return false;

        return nome.equals(that.nome) && duracao.equals(that.duracao) && preco.equals(that.preco);
    }

    @Override
    public int hashCode() {
        int result = nome.hashCode();
        result = 31 * result + duracao.hashCode();
        result = 31 * result + preco.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ServicoDTO{" +
                "nome='" + nome + '\'' +
                ", email='" + duracao + '\'' +
                ", preco=" + preco +
                '}';
    }

}
