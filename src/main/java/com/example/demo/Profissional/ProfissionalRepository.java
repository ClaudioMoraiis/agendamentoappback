package com.example.demo.Profissional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfissionalRepository extends JpaRepository<ProfissionalVO, Long> {
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByCelularAndIdNot(String mCelular, Long id);
    ProfissionalVO findByCelular(String mCelular);
    ProfissionalVO findByEmail(String mEmail);
    ProfissionalVO findByNome(String mNome);
    ProfissionalVO findFirstByNome(String mNome);
}
