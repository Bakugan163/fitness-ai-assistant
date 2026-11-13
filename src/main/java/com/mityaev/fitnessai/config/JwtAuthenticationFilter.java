package com.mityaev.fitnessai.config;

import java.io.IOException;
import java.util.Collections;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component // Говорим Спрингу, что это бин
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // OncePerRequestFilter гарантирует, что фильтр сработает ровно один раз за запрос

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Берем заголовок Authorization (там обычно пишут "Bearer <токен>")
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Если заголовка нет или он не начинается с "Bearer ", пропускаем запрос дальше
        // (Например, это запрос на логин или H2 Console, где токена нет)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Отрезаем слово "Bearer " (7 символов) и оставляем только сам токен
        jwt = authHeader.substring(7);

                try {
            userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtService.isTokenValid(jwt, userEmail)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userEmail,
                            null,
                            Collections.emptyList()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            System.out.println("ОШИБКА ВАЛИДАЦИИ ТОКЕНА: " + e.getMessage());
            // ВАЖНО: Если токен невалиден, мы прерываем запрос и возвращаем 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return; 
        }

        filterChain.doFilter(request, response);

    
}

}