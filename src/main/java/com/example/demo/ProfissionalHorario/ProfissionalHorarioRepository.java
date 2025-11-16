package com.example.demo.ProfissionalHorario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;

public interface ProfissionalHorarioRepository extends JpaRepository<ProfissionalHorarioVO, Long> {
    boolean existsByProfissionalVO_IdAndDiaSemanaAndHoraInicialAndHoraFinal(
            Long profissionalId,
            String diaSemana,
            LocalTime horaInicial,
            LocalTime horaFinal
    );


}
