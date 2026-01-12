package com.example.demo.Mensagem;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Transactional
    @Query(
            value = """
                    UPDATE mensagem
                    SET men_status = 'READ'
                    WHERE (men_sender_id = :mId)
                    AND (men_status <> 'READ')
                    """,
            nativeQuery = true
    )
    void marcarComoLida(@Param("mId") Long mId);

    @Query(
            value = """
                    SELECT *
                    FROM mensagem
                    WHERE (men_cliente_id = :mClientId)
                    ORDER BY men_dthr ASC
                    """,
            countQuery = """
                                SELECT COUNT(*)
                                FROM mensagem
                                WHERE men_cliente_id = :mClientId
                    """,
            nativeQuery = true
    )
    Page<MensagemVO> buscarPorCliente(@Param("mClientId") Long mClientId, Pageable mPageable);

    @Query("""
            SELECT m FROM MensagemVO m
            WHERE
            (
                m.sender.id = :userId
                AND m.deletedBySender = false
            )
            OR
            (
                m.client.id = :userId
                AND m.deletedByRecipient = false
            )
            ORDER BY m.createdAt ASC
            """)
    Page<MensagemVO> buscarConversacao(
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Modifying
    @Transactional
    @Query(
            value = """
                    DELETE mensagem
                    WHERE (men_sender_id = :mIdCliente)
                    """,
            nativeQuery = true
    )
    void deletarVarias(@Param("mIdCliente") Long mIdCliente);

    @Modifying
    @Transactional
    @Query(
            value = """
                    DELETE FROM mensagem
                    WHERE (men_id= :mId)
                    AND (men_status <> 'READ')
                    """,
            nativeQuery = true
    )
    void deletar(@Param("mId") Long mId);

    @Query("""
            SELECT m FROM MensagemVO m
            WHERE 
                ((m.sender.id = :clienteId AND m.client.id = :usuarioId) 
                 OR (m.sender.id = :usuarioId AND m.client.id = :clienteId))
            AND (
                (m.sender.id = :usuarioId AND m.deletedBySender = false)
                OR (m.client.id = :usuarioId AND m.deletedByRecipient = false)
            )
            ORDER BY m.createdAt ASC
            """)
    Page<MensagemVO> buscarConversacaoComFiltro(
            @Param("clienteId") Long clienteId,
            @Param("usuarioId") Long usuarioId,
            Pageable pageable
    );

    @Query("""
    SELECT m FROM MensagemVO m
    WHERE (m.sender.id = :clienteId AND m.client.id = :usuarioId)
       OR (m.sender.id = :usuarioId AND m.client.id = :clienteId)
    """)
    List<MensagemVO> buscarTodasMensagensDaConversa(
            @Param("clienteId") Long clienteId,
            @Param("usuarioId") Long usuarioId
    );
}
