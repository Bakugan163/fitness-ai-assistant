package com.mityaev.fitnessai.config; // Оставил в config, чтобы не плодить папки

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // @Value берет значение из application.yml
    @Value("${app.jwt.secret}")
    private String secretKey;

    @Value("${app.jwt.expirationMs}")
    private long jwtExpiration;

    // Этот метод генерирует токен для пользователя (принимает его email)
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email) // Кладем в токен email как "subject" (владельца токена)
                .issuedAt(new Date(System.currentTimeMillis())) // Время создания токена
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Время, когда токен протухнет
                .signWith(getSignInKey()) // Подписываем нашим секретным ключом
                .compact(); // Превращаем всё в финальную строку токена
    }

    // Вспомогательный метод: превращает строку из конфига в секретный ключ
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

        // Достаем email (subject) из токена
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Проверяем, валиден ли токен (совпадает ли email и не истек ли срок)
    public boolean isTokenValid(String token, String userEmail) {
        final String username = extractUsername(token);
        return (username.equals(userEmail)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // Вспомогательный метод для парсинга токена
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey()) // Проверяем подпись нашим ключом
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}