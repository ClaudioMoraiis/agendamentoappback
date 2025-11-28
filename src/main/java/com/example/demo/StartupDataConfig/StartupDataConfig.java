package com.example.demo.StartupDataConfig;

import com.example.demo.Usuario.UsuarioRepository;
import com.example.demo.Usuario.UsuarioVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class StartupDataConfig {
    @Autowired
    private PasswordEncoder fPasswordEncoder;
    @Bean
    public CommandLineRunner initData(UsuarioRepository mUsuarioRepository) {
        return args -> {
            if (mUsuarioRepository.count() == 0) {
                UsuarioVO mUsuarioVO = new UsuarioVO();
                mUsuarioVO.setNome("ADM");
                mUsuarioVO.setCpf("000.000.000-25");
                mUsuarioVO.setCelular("(48) 999965858");
                mUsuarioVO.setEmail("ADM@GMAIL.COM");
                mUsuarioVO.setSenha(fPasswordEncoder.encode("123"));
                mUsuarioRepository.save(mUsuarioVO);
            }
        };
    }
}
