package com.example.demo.Especialidade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadeRepository extends JpaRepository<EspecialidadeVO, Long> {
    EspecialidadeVO findByNome(String mNome);

}
