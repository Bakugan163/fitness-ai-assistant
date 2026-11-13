package com.mityaev.fitnessai.users.exception;

// Наследуемся от RuntimeException, чтобы Spring смог перехватить это исключение
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

    
}