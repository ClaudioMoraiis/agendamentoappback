package com.example.demo.Usuario;

import jakarta.persistence.*;

@Entity
@Table(name = "USUARIO")
public class UsuarioVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usu_id")
    private Long id;

    @Column(name = "usu_nome", length = 80)
    private String nome;

    @Column(name = "usu_email")
    private String email;

    @Column(name = "usu_senha")
    private String senha;


    public UsuarioVO(Long id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public UsuarioVO(){};

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof UsuarioVO usuarioVO)) return false;

        return id.equals(usuarioVO.id) && nome.equals(usuarioVO.nome) && email.equals(usuarioVO.email) && senha.equals(usuarioVO.senha);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + nome.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + senha.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UsuarioVO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                '}';
    }
}
