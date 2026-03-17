package com.example.taskmanagerapi.entity;
// Пакет, где находятся entity — классы, которые соответствуют таблицам базы данных

import jakarta.persistence.*;
// Аннотации JPA для связи класса с таблицей базы

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// Lombok — автоматически создаёт getters, setters и конструкторы

import java.time.LocalDateTime;
// Класс для хранения даты и времени

import java.util.ArrayList;
import java.util.List;
// Коллекция List для хранения задач пользователя

@Entity
// Этот класс является сущностью (таблицей в базе данных)

@Table(name = "users")
// Имя таблицы в базе данных

@Data
// Lombok создаёт:
// getters
// setters
// toString
// equals
// hashCode

@NoArgsConstructor
// Пустой конструктор: new User()

@AllArgsConstructor
// Конструктор со всеми полями

public class User {

    @Id
    // Первичный ключ таблицы

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // База данных автоматически создаёт id (1,2,3,4...)

    private Long id;

    /**
     * Уникальный логин пользователя
     */

    @Column(unique = true, nullable = false)
    // unique = true → логин должен быть уникальным
    // nullable = false → поле обязательно

    private String username;

    /**
     * Хэшированный пароль (BCrypt)
     * ⚠️ НИКОГДА не храните пароли в открытом виде!
     */

    @Column(nullable = false)
    // Пароль обязательно должен быть

    private String password;
    // Здесь хранится НЕ настоящий пароль,
    // а его зашифрованная версия (hash)

    /**
     * Роль пользователя: USER или ADMIN
     * Префикс ROLE_ обязателен для Spring Security
     */

    @Column(nullable = false)
    // Роль обязательно должна быть

    private String role = "ROLE_USER";
    // По умолчанию новый пользователь получает роль USER

    /**
     * Активен ли аккаунт
     */

    @Column(nullable = false)
    // Поле обязательно

    private Boolean enabled = true;
    // true = аккаунт активен
    // false = аккаунт отключён

    /**
     * Задачи пользователя
     */

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    // Связь "один пользователь → много задач"

    // mappedBy = "owner"
    // означает, что связь управляется полем owner в классе Task

    // cascade = ALL
    // если удалить пользователя → удалятся и его задачи

    // orphanRemoval = true
    // если задача убрана из списка задач пользователя → она удаляется из базы

    private List<Task> tasks = new ArrayList<>();
    // Список задач пользователя
    // ArrayList создаётся сразу, чтобы список не был null

    @Column(nullable = false, updatable = false)
    // createdAt обязательно
    // и его нельзя изменить после создания

    private LocalDateTime createdAt;
    // Время создания пользователя

    @PrePersist
    // Этот метод автоматически вызывается перед сохранением в базу

    protected void onCreate() {
        createdAt = LocalDateTime.now();
        // Когда пользователь создаётся — автоматически ставим текущую дату
    }
}
