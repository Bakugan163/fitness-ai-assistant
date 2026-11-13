package com.mityaev.fitnessai.profiles.dto;

import com.mityaev.fitnessai.profiles.entity.ActivityLevel;
import com.mityaev.fitnessai.profiles.entity.ExperienceLevel;
import com.mityaev.fitnessai.profiles.entity.Gender;

public record CreateClientProfileRequest(
    Gender gender,
    Integer age,
    Double heightCm,
    Double weightKg,
    ActivityLevel activityLevel,
    ExperienceLevel experienceLevel
) {}