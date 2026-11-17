package com.mityaev.fitnessai.profiles.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.mityaev.fitnessai.profiles.entity.ActivityLevel;
import com.mityaev.fitnessai.profiles.entity.Equipment;
import com.mityaev.fitnessai.profiles.entity.ExperienceLevel;
import com.mityaev.fitnessai.profiles.entity.FitnessGoal;
import com.mityaev.fitnessai.profiles.entity.Gender;
import com.mityaev.fitnessai.profiles.entity.TrainingPlace;

public record ClientProfileResponse(
    UUID id,
    String userEmail,
    Gender gender,
    Integer age,
    Double heightCm,
    Double weightKg,
    ActivityLevel activityLevel,
    ExperienceLevel experienceLevel,
    TrainingPlace trainingPlace,          // НОВОЕ
    List<FitnessGoal> goals,              // НОВОЕ
    List<Equipment> equipment,            // НОВОЕ
    LocalDateTime createdAt
) {}