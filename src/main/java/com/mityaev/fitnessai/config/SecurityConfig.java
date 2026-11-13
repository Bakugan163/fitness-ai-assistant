package com.mityaev.fitnessai.config;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Отключаем CSRF (для REST API с JWT это стандартная практика и обязательно)
            .csrf(AbstractHttpConfigurer::disable)
            
            // 2. Настраиваем правила доступа к эндпоинтам
            .authorizeHttpRequests(auth -> auth
                // Консоль H2
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                
                // Swagger UI и документация
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                
                // Обработчик ошибок Spring Boot (ОЧЕНЬ ВАЖНО, иначе 403 замаскирует реальные ошибки)
                .requestMatchers("/error").permitAll()
                
                // Логин и регистрация
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                
                // Создание профиля требует токена (мы уже авторизованы, когда шлем запрос)
                .requestMatchers(HttpMethod.POST, "/api/v1/profiles").authenticated()
                
                // Все остальные запросы требуют аутентификации
                .anyRequest().authenticated()
            )
            
            // 3. Отключаем создание HTTP-сессий (мы stateless, используем JWT)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // 4. Настраиваем возврат 401 (Unauthorized) вместо 403 (Forbidden) при проблемах с токеном
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            
            // 5. Добавляем наш JWT фильтр перед стандартным фильтром аутентификации
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            
            // 6. Разрешаем фреймы для H2 Console
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