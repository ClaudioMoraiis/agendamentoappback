package com.example.demo.Profissional;

import com.example.demo.Especialidade.EspecialidadeVO;
import com.example.demo.Usuario.UsuarioVO;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "PROFISSIONAL")
public class ProfissionalVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pro_id")
    private Long id;

    @Column(name = "pro_nome")
    private String nome;

    @Column(name = "pro_email")
    private String email;

    @Column(name = "pro_celular")
    private String celular;

    @Column(name = "pro_status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "pro_esp_id", referencedColumnName = "esp_id", nullable = false)
    private EspecialidadeVO especialidadeVO;

    public ProfissionalVO(Long id, String nome, String email, String celular, String status, EspecialidadeVO especialidadeVO) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.celular = celular;
        this.status = status;
        this.especialidadeVO = especialidadeVO;
    }

    public ProfissionalVO(){}

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

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EspecialidadeVO getEspecialidadeVO() {
        return especialidadeVO;
    }

    public void setEspecialidadeVO(EspecialidadeVO especialidadeVO) {
        this.especialidadeVO = especialidadeVO;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProfissionalVO that = (ProfissionalVO) o;
        return Objects.equals(id, that.id) && Objects.equals(nome, that.nome) && Objects.equals(email, that.email) && Objects.equals(celular, that.celular) && Objects.equals(status, that.status) && Objects.equals(especialidadeVO, that.especialidadeVO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, email, celular, status, especialidadeVO);
    }

    @Override
    public String toString() {
        return "ProfissionalVO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", celular='" + celular + '\'' +
                ", status='" + status + '\'' +
                ", especialidadeVO=" + especialidadeVO +
                '}';
    }

    @PrePersist
    @PreUpdate
    public void toUpperCase(){
        if (nome != null){
            nome = nome.toUpperCase();
        }

        if (email != null){
            email = email.toUpperCase();
        }

        if (status != null){
            status = status.toUpperCase();
        }
    }
}
