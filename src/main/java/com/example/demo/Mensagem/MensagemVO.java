package com.example.demo.Mensagem;

import com.example.demo.Enum.MessageStatusEnum;
import com.example.demo.Usuario.UsuarioVO;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "MENSAGEM")
public class MensagemVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "men_id", nullable = false)
    private Long id;

    // Quem enviou a mensagem (CLIENT ou ADMIN)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "men_sender_id", referencedColumnName = "usu_id", nullable = false)
    private UsuarioVO sender;

    // Cliente dono da conversa
    // Mesmo quando ADM envia, isso aponta para o CLIENTE
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "men_cliente_id", referencedColumnName = "usu_id", nullable = false)
    private UsuarioVO client;

    @Column(name = "men_conteudo", nullable = false)
    private String conteudo;

    @Enumerated(EnumType.STRING)
    @Column(name = "men_status")
    private MessageStatusEnum status;

    @Column(name = "men_dthr")
    private LocalDateTime createdAt;

    public MensagemVO(Long id, UsuarioVO sender, UsuarioVO client, String conteudo, MessageStatusEnum status, LocalDateTime createdAt) {
        this.id = id;
        this.sender = sender;
        this.client = client;
        this.conteudo = conteudo;
        this.status = status;
        this.createdAt = createdAt;
    }

    public MensagemVO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioVO getSender() {
        return sender;
    }

    public void setSender(UsuarioVO sender) {
        this.sender = sender;
    }

    public UsuarioVO getClient() {
        return client;
    }

    public void setClient(UsuarioVO client) {
        this.client = client;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public MessageStatusEnum getStatus() {
        return status;
    }

    public void setStatus(MessageStatusEnum status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "MensagemVO{" +
                "id=" + id +
                ", sender=" + sender +
                ", client=" + client +
                ", conteudo='" + conteudo + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof MensagemVO that)) return false;

        return Objects.equals(id, that.id) && Objects.equals(sender, that.sender) && Objects.equals(client, that.client) && Objects.equals(conteudo, that.conteudo) && status == that.status && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(sender);
        result = 31 * result + Objects.hashCode(client);
        result = 31 * result + Objects.hashCode(conteudo);
        result = 31 * result + Objects.hashCode(status);
        result = 31 * result + Objects.hashCode(createdAt);
        return result;
    }

    @PrePersist
    @PreUpdate
    public void toUpperCase(){
        if (conteudo != null){
            conteudo = conteudo.toUpperCase();
        }
    }
}
