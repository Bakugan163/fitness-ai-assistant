package com.mityaev.fitnessai.users.dto;

public record LoginRequest(
    String email,
    String password
) {}