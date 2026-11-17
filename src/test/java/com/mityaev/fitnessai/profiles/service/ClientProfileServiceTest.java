package com.mityaev.fitnessai.profiles.service;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mityaev.fitnessai.profiles.dto.ClientProfileResponse;
import com.mityaev.fitnessai.profiles.dto.CreateClientProfileRequest;
import com.mityaev.fitnessai.profiles.entity.ActivityLevel;
import com.mityaev.fitnessai.profiles.entity.ClientProfile;
import com.mityaev.fitnessai.profiles.entity.ExperienceLevel;
import com.mityaev.fitnessai.profiles.entity.Gender;
import com.mityaev.fitnessai.profiles.exception.ProfileAlreadyExistsException;
import com.mityaev.fitnessai.profiles.repository.ClientProfileRepository;
import com.mityaev.fitnessai.users.entity.User;
import com.mityaev.fitnessai.users.repository.UserRepository;

@ExtendWith(MockitoExtension.class) // АННОТАЦИЯ: Говорит JUnit, что мы используем Mockito для создания фейков
public class ClientProfileServiceTest {

    @Mock // АННОТАЦИЯ: Создай фейковый объект этого интерфейса (без реального подключения к БД)
    private ClientProfileRepository clientProfileRepository;

    @Mock // АННОТАЦИЯ: Создай еще один фейк
    private UserRepository userRepository;

    @InjectMocks // АННОТАЦИЯ: Возьми фейки выше и передай их в конструктор этого сервиса
    private ClientProfileService clientProfileService;

    // ТУТ БУДУТ САМИ ТЕСТЫ
@Test // АННОТАЦИЯ: Помечает метод как тестовый
    public void shouldCreateProfileSuccessfully() {
        // 1. ARRANGE (Подготовка данных)
        String userEmail = "test@example.com";
        
        // Готовим фейкового юзера, как будто достали из базы
        User user = new User();
        user.setEmail(userEmail);

        // Готовим запрос от клиента
        CreateClientProfileRequest request = new CreateClientProfileRequest(
                Gender.MALE, 25, 180.5, 80.0, ActivityLevel.MODERATE, ExperienceLevel.BEGINNER
        );

        // УЧИМ НАШИ ФЕЙКИ ПРАВИЛЬНО ОТВЕЧАТЬ:
        // Когда сервис спросит у userRepository.findByEmail, верни фейкового юзера
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        // Когда сервис проверит existsByUserEmail, скажи "нет, профиля нет"
        when(clientProfileRepository.existsByUserEmail(userEmail)).thenReturn(false);
        
        // Когда сервис вызовет save, мы должны вернуть сохраненный профиль.
        // Имитируем поведение базы данных, которая присваивает ID.
        when(clientProfileRepository.save(any(ClientProfile.class))).thenAnswer(invocation -> {
            ClientProfile profileToSave = invocation.getArgument(0);
            profileToSave.setId(UUID.randomUUID()); // База обычно генерирует ID
            return profileToSave;
        });

        // 2. ACT (Выполнение действия)
        ClientProfileResponse response = clientProfileService.createProfile(request, userEmail);

        // 3. ASSERT (Проверка результатов)
        assertNotNull(response); // Ответ не должен быть null
        assertEquals(userEmail, response.userEmail()); // Email должен совпадать
        assertEquals(25, response.age()); // Возраст должен быть 25
        
        // Проверяем, что мы действительно вызывали метод save в базе ровно 1 раз
        verify(clientProfileRepository, times(1)).save(any(ClientProfile.class));
    }

    @Test
    public void shouldThrowExceptionWhenProfileAlreadyExists() {
        // 1. ARRANGE (Подготовка)
        String userEmail = "existing@example.com";
        
        User user = new User();
        user.setEmail(userEmail);

        CreateClientProfileRequest request = new CreateClientProfileRequest(
                Gender.MALE, 25, 180.5, 80.0, ActivityLevel.MODERATE, ExperienceLevel.BEGINNER
        );

        // Учим фейк userRepository возвращать юзера
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        
        // ГЛАВНОЕ: Учим фейк clientProfileRepository говорить, что профиль УЖЕ СУЩЕСТВУЕТ
        when(clientProfileRepository.existsByUserEmail(userEmail)).thenReturn(true);

        // 2. ACT & 3. ASSERT (Действие и Проверка)
        // Мы говорим JUnit: "Выполни этот код и убедись, что он выбросит исключение ProfileAlreadyExistsException"
        ProfileAlreadyExistsException exception = assertThrows(
                ProfileAlreadyExistsException.class,
                () -> clientProfileService.createProfile(request, userEmail)
        );

        // Проверяем, что текст ошибки правильный
        assertEquals("Профиль для пользователя existing@example.com уже существует", exception.getMessage());

        // И САМОЕ ВАЖНОЕ: проверяем, что метод save() в репозитории НЕ ВЫЗЫВАЛСЯ (never)
        // Ведь если профиль уже есть, мы не должны пытаться сохранить его снова!
        verify(clientProfileRepository, never()).save(any(ClientProfile.class));
    }


}