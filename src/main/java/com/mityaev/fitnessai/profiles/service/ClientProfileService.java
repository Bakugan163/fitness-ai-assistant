package com.mityaev.fitnessai.profiles.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mityaev.fitnessai.profiles.dto.ClientProfileResponse;
import com.mityaev.fitnessai.profiles.dto.CreateClientProfileRequest;
import com.mityaev.fitnessai.profiles.entity.ClientProfile;
import com.mityaev.fitnessai.profiles.entity.FitnessGoal;
import com.mityaev.fitnessai.profiles.exception.InvalidAgeException;
import com.mityaev.fitnessai.profiles.exception.InvalidGoalCombinationException;
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

        validateGoals(request.goals());

        // 4. Создаем сущность профиля и заполняем её данными
        ClientProfile profile = new ClientProfile();
        profile.setUser(user); // Привязываем профиль к найденному пользователю
        profile.setGender(request.gender());
        profile.setAge(request.age());
        profile.setHeightCm(request.heightCm());
        profile.setWeightKg(request.weightKg());
        profile.setActivityLevel(request.activityLevel());
        profile.setExperienceLevel(request.experienceLevel());
        profile.setTrainingPlace(request.trainingPlace());
        profile.setGoals(request.goals() != null ? request.goals() : new java.util.ArrayList<>());
        profile.setEquipment(request.equipment() != null ? request.equipment() : new java.util.ArrayList<>());

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
                profile.getTrainingPlace(), 
                profile.getGoals(),         
                profile.getEquipment(),     
                profile.getCreatedAt()
        );
    }

        // Валидация: проверяем, что клиент не выбрал противоречащие друг другу цели
    private void validateGoals(List<FitnessGoal> goals) {
        if (goals == null || goals.isEmpty()) {
            return; // Если цели не передали, пропускаем (или можно кинуть ошибку)
        }

        // Если выбрано "Похудение" и "Набор массы" одновременно
        if (goals.contains(FitnessGoal.WEIGHT_LOSS) && goals.contains(FitnessGoal.MUSCLE_GAIN)) {
            throw new InvalidGoalCombinationException("Нельзя одновременно худеть и набирать мышечную массу");
        }
        // Если выбрано "Похудение" и "Поддержание"
        if (goals.contains(FitnessGoal.WEIGHT_LOSS) && goals.contains(FitnessGoal.MAINTENANCE)) {
            throw new InvalidGoalCombinationException("Нельзя одновременно худеть и поддерживать вес");
        }
        // Если выбрано "Набор массы" и "Сушка"
        if (goals.contains(FitnessGoal.MUSCLE_GAIN) && goals.contains(FitnessGoal.CUTTING)) {
            throw new InvalidGoalCombinationException("Нельзя одновременно набирать массу и сушиться");
        }
        // И так далее по нашему ТЗ... (можно добавить остальные проверки)
    }
}