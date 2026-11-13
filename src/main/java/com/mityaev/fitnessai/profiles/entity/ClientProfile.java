package com.mityaev.fitnessai.profiles.entity;

// ИМПОРТЫ: стандарт JPA для работы с БД
import java.time.LocalDateTime;
import java.util.UUID; // * означает, что мы импортируем все аннотации JPA (Entity, Table, Id и т.д.)

import com.mityaev.fitnessai.users.entity.User; // Для работы с датой и временем создания/обновления

import jakarta.persistence.Column;          // Для уникального ID профиля
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity // АННОТАЦИЯ: Говорит Hibernate, что этот класс — сущность БД. Из него будет создана таблица.
@Table(name = "client_profiles") // АННОТАЦИЯ: Задает точное имя таблицы в базе данных (в snake_case, как принято в SQL).
public class ClientProfile {

    @Id // АННОТАЦИЯ: Указывает, что это поле — первичный ключ (уникальный идентификатор записи).
    @GeneratedValue(strategy = GenerationType.UUID) // АННОТАЦИЯ: Hibernate сам сгенерирует UUID при создании записи.
    private UUID id;

    // АННОТАЦИЯ @OneToOne: Устанавливает связь "Один к Одному" между ClientProfile и User.
    // То есть один пользователь может иметь только один профиль, и наоборот.
    // fetch = FetchType.LAZY: "Ленивая загрузка". Hibernate НЕ будет загружать данные пользователя из БД,
    // пока мы явно не вызовем метод profile.getUser(). Это экономит ресурсы сервера и время запроса.
    @OneToOne(fetch = FetchType.LAZY)
    // АННОТАЦИЯ @JoinColumn: Указывает, как именно создавать колонку в БД для связи.
    // name = "user_id": колонка в таблице client_profiles будет называться user_id.
    // nullable = false: профиль не может существовать без привязки к пользователю (NOT NULL).
    // unique = true: гарантирует на уровне БД, что два профиля не будут ссылаться на одного юзера.
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING) // АННОТАЦИЯ: Говорит сохранять Enum в базу как строку (MALE/FEMALE), а не как порядковый номер (0/1). Это защищает от ошибок при изменении порядка в Enum.
    @Column(nullable = false)    // АННОТАЦИЯ: Поле обязательно для заполнения (NOT NULL).
    private Gender gender;

    @Column(nullable = false)
    private Integer age;

    // @Column(name = "height_cm"): В Java поле называется heightCm (camelCase), 
    // а в базе данных колонка будет height_cm (snake_case). Так принято в SQL.
    @Column(name = "height_cm", nullable = false)
    private Double heightCm;

    @Column(name = "weight_kg", nullable = false)
    private Double weightKg;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_level", nullable = false)
    private ActivityLevel activityLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level", nullable = false)
    private ExperienceLevel experienceLevel;

    // updatable = false: Мы не сможем изменить дату создания после того, как запись сохранена.
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ПУСТОЙ КОНСТРУКТОР: Обязателен для Hibernate, чтобы он мог создать объект при чтении из БД.
    public ClientProfile() {
    }

    // АННОТАЦИЯ @PrePersist: Этот метод срабатывает автоматически ПЕРЕД тем, как Hibernate сохранит (INSERT) объект в БД.
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now(); // Ставим время создания
        this.updatedAt = LocalDateTime.now(); // И время обновления приравниваем к созданию
    }

    // АННОТАЦИЯ @PreUpdate: Этот метод срабатывает автоматически ПЕРЕД обновлением (UPDATE) объекта в БД.
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now(); // Обновляем время последнего изменения
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Double heightCm) {
        this.heightCm = heightCm;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public ActivityLevel getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
    }

    public ExperienceLevel getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(ExperienceLevel experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

   
}