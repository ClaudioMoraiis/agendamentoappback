package com.example.demo.Servico;

import jakarta.persistence.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Entity
@Table(name = "SERVICOS")
public class ServicoVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ser_id")
    private Long id;

    @Column(name = "ser_nome")
    private String nome;

    @Column(name = "ser_duracao")
    private Long duracao;

    @Column(name = "ser_valor")
    private BigDecimal valor;

    public ServicoVO(Long id, String nome, Long duracao, BigDecimal valor) {
        this.id = id;
        this.nome = nome;
        this.duracao = duracao;
        this.valor = valor;
    }

    public ServicoVO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getDuracao() {
        return duracao;
    }

    public void setDuracao(Long duracao) {
        this.duracao = duracao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ServicoVO servicoVO)) return false;

        return id.equals(servicoVO.id) && nome.equals(servicoVO.nome) && duracao.equals(servicoVO.duracao) && valor.equals(servicoVO.valor);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + nome.hashCode();
        result = 31 * result + duracao.hashCode();
        result = 31 * result + valor.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ServicoVO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", duracao='" + duracao + '\'' +
                ", valor=" + valor +
                '}';
    }

    @PrePersist
    @PreUpdate
    public void toUpperCase() {
        if (nome != null) {
            nome = nome.toUpperCase();
        }
    }
}
