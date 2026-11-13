package com.mityaev.fitnessai.users.exception;

// Наследуемся от RuntimeException, чтобы Spring смог перехватить это исключение
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    
}