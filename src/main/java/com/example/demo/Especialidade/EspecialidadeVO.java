package com.example.demo.Especialidade;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "ESPECIALIDADE")
public class EspecialidadeVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "esp_id")
    private Long id;

    @Column(name = "esp_nome")
    private String nome;

    public EspecialidadeVO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public EspecialidadeVO(){}

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EspecialidadeVO that = (EspecialidadeVO) o;
        return Objects.equals(id, that.id) && Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }

    @Override
    public String toString() {
        return "EspecialidadeVO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }

    @PrePersist
    @PreUpdate
    public void toUpperCase(){
        if (nome != null){
            nome = nome.toUpperCase();
        }
    }
}
