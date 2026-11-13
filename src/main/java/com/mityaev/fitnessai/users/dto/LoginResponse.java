package com.mityaev.fitnessai.users.dto;

public record LoginResponse(
    String token // Сюда мы положим наш JWT
) {}