package com.example.demo.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioVO, Long> {
    UsuarioVO findByEmail(String mEmail);
    UsuarioVO findByCpf(String mCpf);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByCpfAndIdNot(String email, Long id);
    UsuarioVO findByNome(String mNome);
}
