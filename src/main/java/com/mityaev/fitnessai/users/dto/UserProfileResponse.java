package com.mityaev.fitnessai.users.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.mityaev.fitnessai.users.entity.Role;

// Record для ответа профиля. Без пароля!
public record UserProfileResponse(
    UUID id,
    String name,
    String email,
    String phoneNumber,
    Role role,
    LocalDateTime createdAt
) {}