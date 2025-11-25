package com.example.demo.Profissional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfissionalRepository extends JpaRepository<ProfissionalVO, Long> {
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByCelularAndIdNot(String mCelular, Long id);
    boolean existsByNomeAndIdNot(String mNome, Long id);
    ProfissionalVO findByCelular(String mCelular);
    ProfissionalVO findByEmail(String mEmail);
    ProfissionalVO findByNome(String mNome);
    ProfissionalVO findFirstByNome(String mNome);
    boolean existsByNome(String mNome);
    @Query(
            value =
                    """
                        SELECT pro_ativo
                        FROM profissional
                        WHERE (pro_id = :mId)
                    """,
            nativeQuery = true
    )
    String getAtivo(@Param("mId") Long mId);

    @Query(
            value =
                    """
                        SELECT age_id
                        FROM agendamento
                        WHERE (age_pro_id = :mId)
                            AND (age_status in('PENDENTE', 'CONFIRMADO'))
                        LIMIT 1
                    """,
            nativeQuery = true
    )
    Long getAgendemento(@Param("mId") Long mId);
}
