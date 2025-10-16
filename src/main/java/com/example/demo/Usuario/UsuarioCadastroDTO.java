package com.example.demo.Usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class UsuarioCadastroDTO {
    @JsonProperty("nome")
    @NotNull(message = "Informe o campo 'nome no body'")
    private String nome;

    @JsonProperty("email")
    @NotNull(message = "Informe o campo 'email' no body")
    private String email;

    @JsonProperty("celular")
    @NotNull(message = "Informe o campo 'celular' no body")
    private String celular;

    @JsonProperty("cpf")
    @NotNull(message = "Informe o campo 'cpf' no body")
    private String cpf;

    @JsonProperty("senha")
    @NotNull(message = "Informe o campo 'senha' no body")
    private String senha;

    public UsuarioCadastroDTO(String nome, String email, String celular, String cpf, String senha) {
        this.nome = nome;
        this.email = email;
        this.celular = celular;
        this.cpf = cpf;
        this.senha = senha;
    }

    public UsuarioCadastroDTO(){}

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

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof UsuarioCadastroDTO that)) return false;

        return nome.equals(that.nome) && email.equals(that.email) && celular.equals(that.celular) && cpf.equals(that.cpf) && senha.equals(that.senha);
    }

    @Override
    public int hashCode() {
        int result = nome.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + celular.hashCode();
        result = 31 * result + cpf.hashCode();
        result = 31 * result + senha.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UsuarioCadastroDTO{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", celular='" + celular + '\'' +
                ", cpf='" + cpf + '\'' +
                ", senha='" + senha + '\'' +
                '}';
    }
}
