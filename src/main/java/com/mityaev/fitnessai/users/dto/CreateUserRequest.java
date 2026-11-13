package com.mityaev.fitnessai.users.dto;


// Record - это специальный тип класса в Java для хранения данных.
// Он автоматически создает конструктор, геттеры, equals, hashCode и toString.
// Идеальная замена Ломбоку для DTO.
public record CreateUserRequest(

   
    String email,
    
    String password,

    String phoneNumber,

    String name
) {}