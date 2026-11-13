package com.mityaev.fitnessai.config;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Внедряем наш фильтр
    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // H2 Console
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                
                // Swagger
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                
                // Логин и регистрация
                .requestMatchers("/api/v1/auth/**").permitAll()
                
                // ВАЖНО: Разрешаем без токена ТОЛЬКО POST-запрос на регистрацию
                .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                
                // ВСЁ ОСТАЛЬНОЕ (включая GET /api/v1/users/me) - ТОЛЬКО С ТОКЕНОМ!
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**", "/api/v1/auth/**", "/api/v1/users")
            )
            // 2. Отключаем сессии (мы же используем токены)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // 3. СТАВИМ НАШ ФИЛЬТР В ЦЕПОЧКУ ПЕРЕД СТАНДАРТНЫМ ФИЛЬТРОМ ЛОГИНА
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            // 4. Настройки для H2
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}