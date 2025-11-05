package com.example.demo.Servico;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ServicoRepository extends JpaRepository<ServicoVO, Long> {
    @Query("SELECT s FROM ServicoVO s WHERE (s.nome = :mNome) AND (s.valor = :mValor)")
    ServicoVO getServico(@Param("mNome") String mNome, @Param("mValor")BigDecimal mValor);
}
