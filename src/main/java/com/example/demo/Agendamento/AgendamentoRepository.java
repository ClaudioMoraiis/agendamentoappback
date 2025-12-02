package com.example.demo.Agendamento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<AgendamentoVO, Long> {
    List<AgendamentoVO> findByProfissionalVO_IdAndDataAndStatusIn(
            Long profissionalId,
            LocalDate data,
            List<EnumAgendamentoStatus> status
    );

    List<AgendamentoVO> findAllById(Long mId);
}
