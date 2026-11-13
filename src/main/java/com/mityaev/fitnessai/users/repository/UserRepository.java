package com.mityaev.fitnessai.users.repository;

// ИМПОРТЫ
import java.util.Optional; // Наша сущность, с которой будем работать
import java.util.UUID; // Главный интерфейс Spring для работы с БД

import org.springframework.data.jpa.repository.JpaRepository; // Говорит Спрингу: "Это компонент для работы с базой данных"
import org.springframework.stereotype.Repository; // Удобный контейнер, который может содержать объект или быть пустым (защита от NullPointerException)

import com.mityaev.fitnessai.users.entity.User; // Тип данных для нашего ID

@Repository // АННОТАЦИЯ: Спринг создаст бин этого интерфейса и будет внедрять его туда, где он понадобится
public interface UserRepository extends JpaRepository<User, UUID> {
    // extends JpaRepository<User, UUID> - это МАГИЯ Spring Data.
    // Наследуясь от него, мы БЕСПЛАТНО получаем кучу готовых методов:
    // save() - сохранить/обновить, findById() - найти по ID, findAll() - получить всех, delete() - удалить.

    // А теперь кастомные методы. Spring Data JPA умеет читать названия методов и сам генерирует SQL-запрос!
    
    // Найди пользователя по email. 
    // Optional значит, что пользователя с таким email может и не быть (и это нормально, мы не упадем с ошибкой).
    Optional<User> findByEmail(String email);

    // Найди пользователя по номеру телефона (добавили, так как ты добавил поле)
    Optional<User> findByPhoneNumber(String phoneNumber);
}