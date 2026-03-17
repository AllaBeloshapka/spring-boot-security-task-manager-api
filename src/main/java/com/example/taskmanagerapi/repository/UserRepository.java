package com.example.taskmanagerapi.repository;

import com.example.taskmanagerapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Поиск пользователя по username
     * Используется Spring Security для аутентификации
     */
    Optional<User> findByUsername(String username);

    /**
     * Проверка существования пользователя
     * Нужно для предотвращения дубликатов при регистрации
     */
    boolean existsByUsername(String username);
}
