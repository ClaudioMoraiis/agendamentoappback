package com.example.demo.ProfissionalHorario;

import com.example.demo.Profissional.ProfissionalVO;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "HORARIO_PROFISSIONAL")
public class ProfissionalHorarioVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hpr_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hpr_pro_id", referencedColumnName = "pro_id", nullable = false)
    private ProfissionalVO profissionalVO;

    @Column(name = "hpr_dia_semana")
    private String diaSemana;

    @Column(name = "hpr_hora_inicio", scale = 5)
    private LocalTime horaInicial;

    @Column(name = "hpr_hora_final", scale = 5)
    private LocalTime horaFinal;

    public ProfissionalHorarioVO(Long id, ProfissionalVO profissionalVO, String diaSemana, LocalTime horaInicial, LocalTime horaFinal) {
        this.id = id;
        this.profissionalVO = profissionalVO;
        this.diaSemana = diaSemana;
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;
    }

    public ProfissionalHorarioVO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProfissionalVO getProfissionalVO() {
        return profissionalVO;
    }

    public void setProfissionalVO(ProfissionalVO profissionalVO) {
        this.profissionalVO = profissionalVO;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(LocalTime horaInicial) {
        this.horaInicial = horaInicial;
    }

    public LocalTime getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(LocalTime horaFinal) {
        this.horaFinal = horaFinal;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ProfissionalHorarioVO that)) return false;

        return Objects.equals(id, that.id) && Objects.equals(profissionalVO, that.profissionalVO) && Objects.equals(diaSemana, that.diaSemana) && Objects.equals(horaInicial, that.horaInicial) && Objects.equals(horaFinal, that.horaFinal);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(profissionalVO);
        result = 31 * result + Objects.hashCode(diaSemana);
        result = 31 * result + Objects.hashCode(horaInicial);
        result = 31 * result + Objects.hashCode(horaFinal);
        return result;
    }

    @Override
    public String toString() {
        return "ProfissionalHorarioVO{" +
                "id=" + id +
                ", profissionalVO=" + profissionalVO +
                ", diaSemana='" + diaSemana + '\'' +
                ", horaInicial=" + horaInicial +
                ", horaFinal=" + horaFinal +
                '}';
    }

    @PrePersist
    @PreUpdate
    public void toUpperCase(){
        if (diaSemana != null){
            diaSemana = diaSemana.toUpperCase();
        }
    }
}


