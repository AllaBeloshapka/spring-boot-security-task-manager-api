package com.example.taskmanagerapi.entity;
// Пакет, где лежат entity (классы, которые соответствуют таблицам в базе данных)

import com.example.taskmanagerapi.enums.TaskStatus;
// Подключаем enum со статусами задач (TODO, IN_PROGRESS, DONE и т.д.)

import jakarta.persistence.*;
// Импортируем аннотации JPA для работы с базой данных

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// Lombok автоматически создаёт конструкторы, геттеры, сеттеры и другие методы

import java.time.LocalDateTime;
// Класс для работы с датой и временем

import java.util.ArrayList;
import java.util.List;
// Эти импорты сейчас не используются в коде (можно удалить)

@Entity
// Говорим Spring/JPA, что этот класс — сущность,
// то есть он будет храниться в таблице базы данных

@Table(name = "tasks")
// Указываем имя таблицы в базе данных

@Data
// Lombok автоматически создаёт:
// getters
// setters
// toString
// equals
// hashCode

@NoArgsConstructor
// Lombok создаёт пустой конструктор: new Task()

@AllArgsConstructor
// Lombok создаёт конструктор со всеми полями

public class Task {

    @Id
    // Это поле — первичный ключ таблицы

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // База данных сама будет генерировать id (1,2,3,4...)

    private Long id;

    @Column(nullable = false)
    // Это поле обязательно
    // В базе нельзя сохранить задачу без title

    private String title;

    @Column(length = 1000)
    // Максимальная длина текста description — 1000 символов

    private String description;

    @Enumerated(EnumType.STRING)
    // Мы говорим базе данных хранить enum как текст
    // Например: TODO, IN_PROGRESS, DONE

    private TaskStatus status = TaskStatus.TODO;
    // По умолчанию новая задача создаётся со статусом TODO

    /**
     * Владелец задачи
     * Важно для проверки прав доступа!
     */

    @ManyToOne(fetch = FetchType.LAZY)
    // Связь "много задач -> один пользователь"
    // Один пользователь может иметь много задач

    // LAZY означает:
    // пользователь загружается из базы только когда он реально нужен

    @JoinColumn(name = "owner_id", nullable = false)
    // В таблице tasks будет колонка owner_id
    // Она хранит id пользователя

    private User owner;
    // Объект пользователя, которому принадлежит задача

    @Column(nullable = false, updatable = false)
    // createdAt обязательно
    // и его нельзя менять после создания

    private LocalDateTime createdAt;
    // Время создания задачи

    @PrePersist
    // Этот метод автоматически вызывается перед сохранением в базу

    protected void onCreate() {
        createdAt = LocalDateTime.now();
        // При создании задачи автоматически ставим текущее время
    }
}
