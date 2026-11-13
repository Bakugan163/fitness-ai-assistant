package com.mityaev.fitnessai.profiles.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mityaev.fitnessai.profiles.dto.ClientProfileResponse;
import com.mityaev.fitnessai.profiles.dto.CreateClientProfileRequest;
import com.mityaev.fitnessai.profiles.entity.ClientProfile;
import com.mityaev.fitnessai.profiles.exception.InvalidAgeException;
import com.mityaev.fitnessai.profiles.exception.ProfileAlreadyExistsException;
import com.mityaev.fitnessai.profiles.repository.ClientProfileRepository;
import com.mityaev.fitnessai.users.entity.User;
import com.mityaev.fitnessai.users.exception.UserNotFoundException;
import com.mityaev.fitnessai.users.repository.UserRepository;

@Service
public class ClientProfileService {

    private final ClientProfileRepository clientProfileRepository;
    private final UserRepository userRepository;

    public ClientProfileService(ClientProfileRepository clientProfileRepository, UserRepository userRepository) {
        this.clientProfileRepository = clientProfileRepository;
        this.userRepository = userRepository;
    }

    // @Transactional гарантирует, что если во время выполнения метода произойдет ошибка,
    // все изменения в базе данных откатятся (rollback). Это критично для сохранения целостности данных.
    @Transactional
    public ClientProfileResponse createProfile(CreateClientProfileRequest request, String userEmail) {
        
        // 1. Находим пользователя в базе по email (из токена)
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        // 2. БИЗНЕС-ПРАВИЛО: Проверяем, нет ли уже профиля у этого пользователя
        if (clientProfileRepository.existsByUserEmail(userEmail)) {
            throw new ProfileAlreadyExistsException("Профиль для пользователя " + userEmail + " уже существует");
        }

        // 3. БИЗНЕС-ПРАВИЛО: Валидация возраста (из нашего ТЗ - не младше 14 лет)
        if (request.age() < 14) {
            throw new InvalidAgeException("Возраст не может быть меньше 14 лет");
        }

        // 4. Создаем сущность профиля и заполняем её данными
        ClientProfile profile = new ClientProfile();
        profile.setUser(user); // Привязываем профиль к найденному пользователю
        profile.setGender(request.gender());
        profile.setAge(request.age());
        profile.setHeightCm(request.heightCm());
        profile.setWeightKg(request.weightKg());
        profile.setActivityLevel(request.activityLevel());
        profile.setExperienceLevel(request.experienceLevel());

        // 5. Сохраняем в базу
        ClientProfile savedProfile = clientProfileRepository.save(profile);

        // 6. Возвращаем DTO (мапим сущность в ответ)
        return mapToResponse(savedProfile);
    }

    // Вспомогательный метод для превращения Entity в DTO
    private ClientProfileResponse mapToResponse(ClientProfile profile) {
        return new ClientProfileResponse(
                profile.getId(),
                profile.getUser().getEmail(), // Достаем email из связанного юзера
                profile.getGender(),
                profile.getAge(),
                profile.getHeightCm(),
                profile.getWeightKg(),
                profile.getActivityLevel(),
                profile.getExperienceLevel(),
                profile.getCreatedAt()
        );
    }
}