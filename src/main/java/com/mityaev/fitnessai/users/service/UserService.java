package com.mityaev.fitnessai.users.service;

import org.springframework.stereotype.Service;

import com.mityaev.fitnessai.users.dto.CreateUserRequest;
import com.mityaev.fitnessai.users.entity.Role;
import com.mityaev.fitnessai.users.entity.User;
import com.mityaev.fitnessai.users.exception.UserAlreadyExistsException;
import com.mityaev.fitnessai.users.repository.UserRepository;

@Service // Говорит Спрингу: "Это бизнес-логика, управляй этим бином"
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ТЕПЕРЬ ВСЯ ЛОГИКА ЖИВЕТ ЗДЕСЬ
        public User createUser(CreateUserRequest request) {
             // 1. ПРОВЕРКА EMAIL
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Пользователь с email '" + request.email() + "' уже существует");
        }

        // 2. ПРОВЕРКА ТЕЛЕФОНА
        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new UserAlreadyExistsException("Пользователь с номером телефона '" + request.phoneNumber() + "' уже существует");
        }
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(request.password()); 
        user.setPhoneNumber(request.phoneNumber());
        user.setName(request.name()); // ДОБАВИЛИ ЭТУ СТРОКУ
        user.setRole(Role.CLIENT);

        return userRepository.save(user);
    }
}