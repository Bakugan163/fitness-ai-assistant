package com.mityaev.fitnessai.users.controllers;

import com.mityaev.fitnessai.users.dto.LoginRequest;
import com.mityaev.fitnessai.users.dto.LoginResponse;
import com.mityaev.fitnessai.users.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth") // Базовый путь для авторизации
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // Просто вызываем метод из сервиса и возвращаем результат
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}