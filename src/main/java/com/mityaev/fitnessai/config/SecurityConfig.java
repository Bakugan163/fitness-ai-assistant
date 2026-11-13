package com.mityaev.fitnessai.config;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
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
                // Используем PathRequest.toH2Console() — это официальный и надежный способ
                // Он автоматически определяет все внутренние пути H2 Console
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                // Все остальные страницы требуют авторизации
                .anyRequest().authenticated()
            )
            // 2. Отключаем CSRF-защиту ТОЛЬКО для H2 Console
            // H2 Console — это сторонний инструмент, он не умеет работать с CSRF-токенами
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(PathRequest.toH2Console())
            )
            // 3. Разрешаем использование фреймов для того же домена
            // Интерфейс H2 Console построен на HTML-фреймах, которые Spring Security блокирует
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }
}