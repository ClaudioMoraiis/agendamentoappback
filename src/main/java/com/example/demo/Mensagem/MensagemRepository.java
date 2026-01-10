package com.example.demo.Mensagem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MensagemRepository extends JpaRepository<MensagemVO, Long> {
    @Query(
            value =
                    """
                        SELECT *
                        FROM mensagem
                        WHERE (men_cliente_id = :mId)
                        ORDER BY men_dthr ASC
                    """,
            nativeQuery = true
    )
    List<MensagemVO> findByCliente(@Param("mId") Long mId);

}
