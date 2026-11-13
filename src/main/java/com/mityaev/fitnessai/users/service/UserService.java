package com.mityaev.fitnessai.users.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mityaev.fitnessai.config.JwtService;
import com.mityaev.fitnessai.users.dto.CreateUserRequest;
import com.mityaev.fitnessai.users.dto.LoginRequest; // ИМПОРТ
import com.mityaev.fitnessai.users.dto.LoginResponse;
import com.mityaev.fitnessai.users.dto.UserProfileResponse;
import com.mityaev.fitnessai.users.entity.Role;
import com.mityaev.fitnessai.users.entity.User;
import com.mityaev.fitnessai.users.exception.UserAlreadyExistsException;
import com.mityaev.fitnessai.users.exception.UserNotFoundException;
import com.mityaev.fitnessai.users.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; // ДОБАВИЛИ

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Пользователь с email '" + request.email() + "' уже существует");
        }

        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new UserAlreadyExistsException("Пользователь с номером телефона '" + request.phoneNumber() + "' уже существует");
        }

        User user = new User();
        user.setEmail(request.email());
        
        // ГЛАВНОЕ ИЗМЕНЕНИЕ: Шифруем пароль перед сохранением!
        user.setPassword(passwordEncoder.encode(request.password()));
        
        user.setPhoneNumber(request.phoneNumber());
        user.setName(request.name());
        user.setRole(Role.CLIENT);

        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        // 1. Ищем юзера по email. Если нет - кидаем ошибку BadCredentialsException
        // (Мы специально не пишем "Пользователь не найден", чтобы хакеры не могли узнать, какие email у нас есть)
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Неверный email или пароль"));

        // 2. Сравниваем введенный пароль с хешем из базы
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Неверный email или пароль");
        }

        // 3. Если всё ок - генерируем токен
        String token = jwtService.generateToken(user.getEmail());
        
        // 4. Возвращаем ответ (в нашем случае DTO с токеном)
        return new LoginResponse(token);
    }

        // Найти пользователя по его email
        public UserProfileResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с email '" + email + "' не найден"));

        return new UserProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

}