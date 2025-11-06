package com.example.demo.UsuarioToken;

import com.example.demo.Usuario.UsuarioVO;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "USUARIO_TOKEN")
public class UsuarioTokenVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uto_id;

    @ManyToOne
    @JoinColumn(name = "uto_user_id", referencedColumnName = "usu_id", nullable = false)
    private UsuarioVO usuario;

    @Column(name = "uto_token", nullable = false, unique = true)
    private String token;

    @Column(name = "uto_dthr_expiracao", nullable = false)
    private LocalDateTime dthrExpiracao;

    @Column(name = "uto_ativo", nullable = false)
    private Boolean ativo;

    public UsuarioTokenVO(UsuarioVO usuarioVO) {
        if (usuarioVO == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo.");
        }
        this.usuario = usuarioVO;
        this.token = UUID.randomUUID().toString();
        this.dthrExpiracao = LocalDateTime.now().plusHours(1);
    }

    public UsuarioTokenVO() {}

    public boolean isExpirado() {
        return LocalDateTime.now().isAfter(dthrExpiracao);
    }

    public Long getUto_id() {
        return uto_id;
    }

    public void setUto_id(Long uto_id) {
        this.uto_id = uto_id;
    }

    public UsuarioVO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioVO usuario) {
        this.usuario = usuario;
    }

    public String getUto_token() {
        return token;
    }

    public void setUto_token(String uto_token) {
        this.token = uto_token;
    }

    public LocalDateTime getUto_dthr_expiracao() {
        return dthrExpiracao;
    }

    public void setUto_dthr_expiracao(LocalDateTime uto_dthr_expiracao) {
        this.dthrExpiracao = uto_dthr_expiracao;
    }

    public void setUto_ativo(Boolean uto_ativo){
        this.ativo = uto_ativo;
    }

    public Boolean get_uto_ativo(){
        return ativo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario, dthrExpiracao, uto_id, token);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        UsuarioTokenVO other = (UsuarioTokenVO) obj;
        return Objects.equals(usuario, other.usuario) && Objects.equals(dthrExpiracao, other.dthrExpiracao)
                && Objects.equals(uto_id, other.uto_id) && Objects.equals(token, other.token);
    }

    @Override
    public String toString() {
        return "UsuarioToken [uto_id=" + uto_id + ", usuario=" + usuario + ", uto_token=" + token
                + ", uto_dthr_expiracao=" + dthrExpiracao + "]";
    }
}
