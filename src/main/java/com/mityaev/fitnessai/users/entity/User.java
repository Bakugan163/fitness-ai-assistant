package com.mityaev.fitnessai.users.entity;

// ИМПОРТЫ: подключаем классы из стандарта JPA (Java Persistence API)
import java.time.LocalDateTime;              // Говорит: "Этот класс — таблица в базе данных"
import java.util.UUID;               // Позволяет задать точное имя таблицы в БД

import jakarta.persistence.Column;                  // Указывает, какое поле является первичным ключом
import jakarta.persistence.Entity;      // Говорит: "ID будет создаваться автоматически"
import jakarta.persistence.EnumType;      // Указывает стратегию генерации ID (в нашем случае UUID)
import jakarta.persistence.Enumerated;              // Настройки для конкретной колонки (имя, NOT NULL и т.д.)
import jakarta.persistence.GeneratedValue;            // Указывает, как сохранять Enum (как строку или как число)
import jakarta.persistence.GenerationType;          // Применяется к полям-enum
import jakarta.persistence.Id;          // Позволяет выполнить код ПЕРЕД сохранением объекта в БД
import jakarta.persistence.PrePersist;                 // Стандартный Java-класс для даты и времени
import jakarta.persistence.Table;                          // Класс для генерации уникального идентификатора (UUID)

@Entity // АННОТАЦИЯ: Hibernate поймёт, что этот класс надо превратить в таблицу
@Table(name = "users") // В БД таблица будет называться "users" (слово "user" зарезервировано в SQL)
public class User {

    @Id // Это поле — первичный ключ (уникальный номер записи)
    @GeneratedValue(strategy = GenerationType.UUID) // ID будет генерироваться автоматически в формате UUID
    private UUID id;

    @Column(unique = true, nullable = false) // В базе email должен быть уникальным и не пустым (NOT NULL)
    private String email;

    @Column(nullable = false)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false) // Пароль не может быть пустым (NOT NULL)
    private String password;

    // НОВОЕ ПОЛЕ: Номер телефона
    @Column(unique = true, nullable = false, length = 20) 
    // unique = true — двух юзеров с одинаковым номером быть не может
    // nullable = false — номер обязателен для заполнения
    // length = 20 — ограничиваем длину строки в базе (влезет любой формат: +7..., 8... и т.д.)
    private String phoneNumber;

    @Enumerated(EnumType.STRING) // Сохраняй Enum как текст (CLIENT, TRAINER, ADMIN), а не как цифры (0, 1, 2)
    @Column(nullable = false) // Роль обязательна для заполнения
    private Role role;

    @Column(nullable = false) // Статус аккаунта (включен/выключен) тоже обязателен
    private boolean enabled;

    @Column(name = "created_at", nullable = false, updatable = false) 
    // В базе колонка будет называться "created_at" (в Java принят camelCase, в SQL - snake_case)
    // updatable = false означает, что дату создания нельзя будет изменить после сохранения
    private LocalDateTime createdAt;

    // ПУСТОЙ КОНСТРУКТОР: Hibernate требует его для создания объекта, когда читает данные из БД
    public User() {
    }

    @PrePersist // АННОТАЦИЯ: Этот метод сработает АВТОМАТИЧЕСКИ прямо перед тем, как Hibernate сохранит юзера
    protected void onCreate() {
        this.createdAt = LocalDateTime.now(); // Ставим текущую дату и время
        this.enabled = true;                  // По умолчанию делаем аккаунт активным
    }

    // ВНИМАНИЕ: Здесь должны быть геттеры и сеттеры для всех полей!
    // Сгенерируй их через VS Code: кликни правой кнопкой мыши внутри класса -> Source Action -> Generate Getters and Setters -> Выбери все поля.


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
