package com.example.demo.Mensagem;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class MensagemDTO {
    @JsonProperty("clienteId")
    @NotNull(message = "Campo 'clienteId' não informado no body!")
    private Long clienteId;

    @JsonProperty("senderId")
    @NotNull(message = "Campo 'senderId' não informado no body!")
    private Long senderId;

    @JsonProperty("conteudo")
    @NotNull(message = "Campo 'conteudo' não informado no body!")
    private String conteudo;

    @JsonProperty("situacao")
    private String situacao;

    public MensagemDTO(Long clienteId, Long senderId, String conteudo, String situacao) {
        this.clienteId = clienteId;
        this.senderId = senderId;
        this.conteudo = conteudo;
        this.situacao = situacao;
        this.situacao = situacao;
    }

    public MensagemDTO(){}

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof MensagemDTO that)) return false;

        return Objects.equals(clienteId, that.clienteId) && Objects.equals(senderId, that.senderId) && Objects.equals(conteudo, that.conteudo) && Objects.equals(situacao, that.situacao);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(clienteId);
        result = 31 * result + Objects.hashCode(senderId);
        result = 31 * result + Objects.hashCode(conteudo);
        result = 31 * result + Objects.hashCode(situacao);
        return result;
    }

    @Override
    public String toString() {
        return "MensagemDTO{" +
                "clienteId=" + clienteId +
                ", senderId=" + senderId +
                ", situacao=" + situacao +
                ", conteudo='" + conteudo + '\'' +
                '}';
    }
}
