package com.example.demo.ProfissionalHorario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@Repository
public interface ProfissionalHorarioRepository extends JpaRepository<ProfissionalHorarioVO, Long> {
    boolean existsByProfissionalVO_IdAndDiaSemanaAndHoraInicialAndHoraFinal(
            Long profissionalId,
            String diaSemana,
            LocalTime horaInicial,
            LocalTime horaFinal
    );


}
