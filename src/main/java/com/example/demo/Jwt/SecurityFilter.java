package com.example.demo.Jwt;

import com.example.demo.Usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService fTokenSerive;

    @Autowired
    UsuarioRepository fUsuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        // Adicione TODAS as rotas públicas do Swagger
        if (path.startsWith("/usuario/login") ||
                path.startsWith("/usuario/cadastrar") ||
                path.startsWith("/usuario/recuperar-senha") ||
                path.startsWith("/usuario/redefinir-senha") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/swagger-resources") ||
                path.startsWith("/webjars") ||
                path.startsWith("/swagger-ui.html") ||
                path.equals("/swagger-ui/index.html") ||
                path.equals("/swagger-ui/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Restante do código permanece igual...
        var token = this.recoverToken(request);
        if (token != null) {
            try {
                var email = fTokenSerive.validateToken(token);
                UserDetails user = fUsuarioRepository.findByEmail(email);
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }

}