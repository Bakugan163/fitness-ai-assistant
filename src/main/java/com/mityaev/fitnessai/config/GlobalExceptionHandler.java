package com.mityaev.fitnessai.config;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mityaev.fitnessai.users.exception.UserAlreadyExistsException;

@RestControllerAdvice // Говорит Спрингу: "Этот класс перехватывает ошибки со всех контроллеров"
public class GlobalExceptionHandler {

    // Перехватываем именно нашу ошибку
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        // Возвращаем HTTP-статус 409 (Conflict) и JSON с текстом ошибки
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }
}