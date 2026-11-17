package com.mityaev.fitnessai.profiles.dto;

import java.util.List;

import com.mityaev.fitnessai.profiles.entity.ActivityLevel;
import com.mityaev.fitnessai.profiles.entity.Equipment;
import com.mityaev.fitnessai.profiles.entity.ExperienceLevel;
import com.mityaev.fitnessai.profiles.entity.FitnessGoal;
import com.mityaev.fitnessai.profiles.entity.Gender;
import com.mityaev.fitnessai.profiles.entity.TrainingPlace;

public record CreateClientProfileRequest(
    Gender gender,
    Integer age,
    Double heightCm,
    Double weightKg,
    ActivityLevel activityLevel,
    ExperienceLevel experienceLevel,
     TrainingPlace trainingPlace,          
    List<FitnessGoal> goals,              
    List<Equipment> equipment    
) {}