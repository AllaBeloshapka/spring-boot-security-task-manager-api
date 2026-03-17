package com.example.taskmanagerapi.repository;

import com.example.taskmanagerapi.entity.Task;
import com.example.taskmanagerapi.entity.User;
import com.example.taskmanagerapi.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    //Находим все задачи определённого пользователя
    List<Task> findByOwner(User owner);

    //Находим задачи по статусу
    List<Task> findByStatus(TaskStatus status);

    //Находим задачу определённого пользователя по статусу
    List<Task> findByOwnerAndStatus(User owner, TaskStatus status);

    //Находим задачу по слову
    List<Task> findByTitleContainingOrDescriptionContaining(String keyword1, String keyword2);
}
