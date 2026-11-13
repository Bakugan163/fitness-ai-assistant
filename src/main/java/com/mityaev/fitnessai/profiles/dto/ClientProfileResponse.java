package com.mityaev.fitnessai.profiles.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.mityaev.fitnessai.profiles.entity.ActivityLevel;
import com.mityaev.fitnessai.profiles.entity.ExperienceLevel;
import com.mityaev.fitnessai.profiles.entity.Gender;

public record ClientProfileResponse(
    UUID id,
    String userEmail,
    Gender gender,
    Integer age,
    Double heightCm,
    Double weightKg,
    ActivityLevel activityLevel,
    ExperienceLevel experienceLevel,
    LocalDateTime createdAt
) {}