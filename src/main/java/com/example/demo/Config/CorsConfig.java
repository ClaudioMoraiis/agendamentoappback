package com.example.demo.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CorsConfig {

    @Value("${app.allowed.origins:}")
    private String allowedOriginsProperty;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        List<String> origins = new ArrayList<>();
        origins.add("http://localhost:5173");
        origins.add("http://localhost:5174");
        origins.add("http://localhost:5175");
        origins.add("http://localhost:8080");
        origins.add("http://192.168.18.22:5173");
        origins.add("https://agendamento-app-mocha.vercel.app");
        origins.add("https://agendamento.claudiomoraisdev.com.br");
        origins.add("https://www.claudiomoraisdev.com.br");
        origins.add("https://claudiomoraisdev.com.br");

        if (allowedOriginsProperty != null && !allowedOriginsProperty.isBlank()) {
            List<String> extra = Arrays.stream(allowedOriginsProperty.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            origins.addAll(extra);
        }

        config.setAllowedOrigins(origins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
