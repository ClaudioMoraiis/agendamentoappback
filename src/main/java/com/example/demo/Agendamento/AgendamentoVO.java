package com.example.demo.Agendamento;

import com.example.demo.Profissional.ProfissionalVO;
import com.example.demo.Servico.ServicoVO;
import com.example.demo.Usuario.UsuarioVO;
import jakarta.persistence.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "AGENDAMENTO")
public class AgendamentoVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "age_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "age_usu_id", referencedColumnName = "usu_id", nullable = true)
    private UsuarioVO usuarioVO;

    @Column(name = "age_usu_nome")
    private String nomeUsuario;

    @ManyToOne
    @JoinColumn(name = "age_ser_id", referencedColumnName = "ser_id", nullable = false)
    private ServicoVO servicoVO;

    @Column(name = "age_ser_nome")
    private String nomeServico;

    @ManyToOne
    @JoinColumn(name = "age_pro_id", referencedColumnName = "pro_id", nullable = false)
    private ProfissionalVO profissionalVO;

    @Column(name = "age_pro_nome")
    private String nomeProfissional;

    @Column(name = "age_valor")
    private BigDecimal valor;

    @Column(name = "age_data")
    private LocalDate data;

    @Column(name = "age_horario_incio")
    private LocalTime horarioIncio;

    @Column(name = "age_horario_fim")
    private LocalTime horarioFim;

    @Column(name = "age_status")
    @Enumerated(EnumType.STRING)
    private EnumAgendamentoStatus status;

    @Column(name = "agen_usu_cadastrado")
    private String usuarioCadastrado;

    public AgendamentoVO(Long id, UsuarioVO usuarioVO, String nomeUsuario, ServicoVO servicoVO, String nomeServico,
                         ProfissionalVO profissionalVO, String nomeProfissional, BigDecimal valor, LocalDate data,
                         LocalTime horarioIncio, LocalTime horarioFim, EnumAgendamentoStatus status, String usuarioCadastrado) {
        this.id = id;
        this.usuarioVO = usuarioVO;
        this.nomeUsuario = nomeUsuario;
        this.servicoVO = servicoVO;
        this.nomeServico = nomeServico;
        this.profissionalVO = profissionalVO;
        this.nomeProfissional = nomeProfissional;
        this.valor = valor;
        this.data = data;
        this.horarioIncio = horarioIncio;
        this.horarioFim = horarioFim;
        this.status = status;
        this.usuarioCadastrado = usuarioCadastrado;
    }

    public AgendamentoVO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioVO getUsuarioVO() {
        return usuarioVO;
    }

    public void setUsuarioVO(UsuarioVO usuarioVO) {
        this.usuarioVO = usuarioVO;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public ServicoVO getServicoVO() {
        return servicoVO;
    }

    public void setServicoVO(ServicoVO servicoVO) {
        this.servicoVO = servicoVO;
    }

    public String getNomeServico() {
        return nomeServico;
    }

    public void setNomeServico(String nomeServico) {
        this.nomeServico = nomeServico;
    }

    public ProfissionalVO getProfissionalVO() {
        return profissionalVO;
    }

    public void setProfissionalVO(ProfissionalVO profissionalVO) {
        this.profissionalVO = profissionalVO;
    }

    public String getNomeProfissional() {
        return nomeProfissional;
    }

    public void setNomeProfissional(String nomeProfissional) {
        this.nomeProfissional = nomeProfissional;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorarioIncio() {
        return horarioIncio;
    }

    public void setHorarioIncio(LocalTime horarioIncio) {
        this.horarioIncio = horarioIncio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(LocalTime horarioFim) {
        this.horarioFim = horarioFim;
    }

    public EnumAgendamentoStatus getStatus() {
        return status;
    }

    public void setStatus(EnumAgendamentoStatus status) {
        this.status = status;
    }

    public String getUsuarioCadastrado() {
        return usuarioCadastrado;
    }

    public void setUsuarioCadastrado(String usuarioCadastrado) {
        this.usuarioCadastrado = usuarioCadastrado;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof AgendamentoVO that)) return false;

        return Objects.equals(id, that.id) && Objects.equals(usuarioVO, that.usuarioVO) && Objects.equals(nomeUsuario, that.nomeUsuario) && Objects.equals(servicoVO, that.servicoVO) && Objects.equals(nomeServico, that.nomeServico) && Objects.equals(profissionalVO, that.profissionalVO) && Objects.equals(nomeProfissional, that.nomeProfissional) && Objects.equals(valor, that.valor) && Objects.equals(data, that.data) && Objects.equals(horarioIncio, that.horarioIncio) && Objects.equals(horarioFim, that.horarioFim) && status == that.status && Objects.equals(usuarioCadastrado, that.usuarioCadastrado);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(usuarioVO);
        result = 31 * result + Objects.hashCode(nomeUsuario);
        result = 31 * result + Objects.hashCode(servicoVO);
        result = 31 * result + Objects.hashCode(nomeServico);
        result = 31 * result + Objects.hashCode(profissionalVO);
        result = 31 * result + Objects.hashCode(nomeProfissional);
        result = 31 * result + Objects.hashCode(valor);
        result = 31 * result + Objects.hashCode(data);
        result = 31 * result + Objects.hashCode(horarioIncio);
        result = 31 * result + Objects.hashCode(horarioFim);
        result = 31 * result + Objects.hashCode(status);
        result = 31 * result + Objects.hashCode(usuarioCadastrado);
        return result;
    }

    @Override
    public String toString() {
        return "AgendamentoVO{" +
                "id=" + id +
                ", usuarioVO=" + usuarioVO +
                ", nomeUsuario='" + nomeUsuario + '\'' +
                ", servicoVO=" + servicoVO +
                ", nomeServico='" + nomeServico + '\'' +
                ", profissionalVO=" + profissionalVO +
                ", nomeProfissional='" + nomeProfissional + '\'' +
                ", valor=" + valor +
                ", data=" + data +
                ", horarioIncio=" + horarioIncio +
                ", horarioFim=" + horarioFim +
                ", status=" + status +
                ", usuarioCadastrado='" + usuarioCadastrado + '\'' +
                '}';
    }

    @PreUpdate
    @PrePersist
    public void toUpperCase(){
        if (usuarioCadastrado != null){
            usuarioCadastrado = usuarioCadastrado.toUpperCase();
        }
    }
}
