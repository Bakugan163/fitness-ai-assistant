package com.mityaev.fitnessai.config;

import java.util.Map;

import org.springframework.http.HttpStatus; // Новый импорт
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mityaev.fitnessai.profiles.exception.InvalidAgeException;
import com.mityaev.fitnessai.profiles.exception.ProfileAlreadyExistsException;
import com.mityaev.fitnessai.users.exception.UserAlreadyExistsException; // Импорты
import com.mityaev.fitnessai.users.exception.UserNotFoundException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", ex.getMessage()));
    }

    // НОВЫЙ МЕТОД: Если юзер не найден, возвращаем 404
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(ProfileAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleProfileAlreadyExists(ProfileAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 Conflict
                .body(Map.of("error", ex.getMessage()));
    }

    // Ловим ошибку неправильного возраста
    @ExceptionHandler(InvalidAgeException.class)
    public ResponseEntity<Map<String, String>> handleInvalidAge(InvalidAgeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400 Bad Request
                .body(Map.of("error", ex.getMessage()));
    }
}