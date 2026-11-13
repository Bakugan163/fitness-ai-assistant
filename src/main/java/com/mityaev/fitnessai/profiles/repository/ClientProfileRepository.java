package com.mityaev.fitnessai.profiles.repository;

import java.util.Optional;
import java.util.UUID; // Главный интерфейс Spring Data для работы с БД

import org.springframework.data.jpa.repository.JpaRepository; // Говорит Спрингу, что это компонент для работы с БД
import org.springframework.stereotype.Repository; // Контейнер для безопасной работы (может быть пустым)

import com.mityaev.fitnessai.profiles.entity.ClientProfile;     // Тип нашего ID

@Repository // АННОТАЦИЯ: Спринг создаст бин этого интерфейса и внедрит его туда, где он понадобится (в Сервис)
public interface ClientProfileRepository extends JpaRepository<ClientProfile, UUID> {
    // extends JpaRepository<ClientProfile, UUID> дает нам из коробки:
    // save(), findById(), findAll(), delete(), existsById() и т.д.
    // Работать они будут с сущностью ClientProfile, где ID имеет тип UUID.

    // КАСТОМНЫЙ МЕТОД:
    // Так как мы используем JWT, при запросе от пользователя мы знаем только его email.
    // Нам нужно найти профиль, принадлежащий юзеру с таким email.
    // Spring Data JPA умеет "проваливаться" в связанные сущности через название метода!
    // findByUserEmail означает: "Найди ClientProfile, где поле user (связь) имеет поле email = параметр".
    Optional<ClientProfile> findByUserEmail(String email);
    
    // Еще один полезный метод на будущее: проверить, есть ли уже профиль у этого юзера
    boolean existsByUserEmail(String email);
}