package com.example.demo.ProfissionalHorario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface ProfissionalHorarioRepository extends JpaRepository<ProfissionalHorarioVO, Long> {
    boolean existsByProfissionalVO_IdAndDiaSemanaAndHoraInicialAndHoraFinal(
            Long profissionalId,
            String diaSemana,
            LocalTime horaInicial,
            LocalTime horaFinal
    );

    ProfissionalHorarioVO findByProfissionalVO_IdAndDiaSemanaContaining(Long mProfissionalId, String mDayOfWeek);
    List<ProfissionalHorarioVO> findByProfissionalVO_id(Long mId);

    @Query(
            value = """
                SELECT *
                FROM horario_profissional
                WHERE (:mHorario BETWEEN hpr_hora_inicio and hpr_hora_final)
                    """,
            nativeQuery = true
    )
    public ProfissionalHorarioVO getByHorario(@Param("mHorario") @DateTimeFormat(pattern = "HH:mm") LocalTime mHorario);

}
