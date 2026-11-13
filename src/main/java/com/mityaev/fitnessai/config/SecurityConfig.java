package com.mityaev.fitnessai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Говорит Спрингу: "Это класс с настройками"
@EnableWebSecurity // Включает механизм безопасности Spring Security
public class SecurityConfig {

       @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Настраиваем правила доступа
            .authorizeHttpRequests(auth -> auth
                // Разрешаем H2 Console (используем наш проверенный импорт)
                .requestMatchers(org.springframework.boot.security.autoconfigure.web.servlet.PathRequest.toH2Console()).permitAll()
                
                // Разрешаем Swagger
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                
                // Разрешаем доступ к нашему API пользователей
                .requestMatchers("/api/v1/users/**").permitAll()
                
                // Все остальное - только после логина
                .anyRequest().authenticated()
            )
            // 2. Отключаем CSRF для H2 и нашего API (чтобы POST-запросы проходили)
            .csrf(csrf -> csrf
                // Используем простые строки! Это самый надежный способ.
                .ignoringRequestMatchers("/h2-console/**", "/api/v1/users/**")
            )
            // 3. Разрешаем фреймы для H2 Console
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }
}