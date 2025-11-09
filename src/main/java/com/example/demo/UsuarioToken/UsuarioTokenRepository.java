package com.example.demo.UsuarioToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UsuarioTokenRepository extends JpaRepository<UsuarioTokenVO, Long> {
    @Query("SELECT u FROM UsuarioTokenVO u WHERE u.token = :mToken")
    Optional<UsuarioTokenVO> findByToken(@Param("mToken") String token);

    @Query("SELECT ut.token from UsuarioTokenVO ut " + "  INNER JOIN ut.usuario u " + "WHERE (u.email = :mEmail)" + " AND (ut.dthrExpiracao > :mDataHora)")
    String getTokenAtivo(@Param("mEmail") String email, @Param("mDataHora") LocalDateTime dataHora);


    @Query("SELECT u FROM UsuarioTokenVO ut " + "  INNER JOIN ut.usuario u " + "WHERE (u.email = :mEmail)")
    UsuarioTokenVO findByEmail(@Param("mEmail") String email);

    @Query("""
            SELECT ut.token
            FROM UsuarioTokenVO ut
            INNER JOIN ut.usuario u
            WHERE u.email = :mEmail
              AND ut.dthrExpiracao > :mDataHora
            """)
    String buscarTokenValido(@Param("mEmail") String email, @Param("mDataHora") LocalDateTime dataHora);

}
