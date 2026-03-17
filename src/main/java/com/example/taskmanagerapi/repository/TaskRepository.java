package com.example.taskmanagerapi.repository;

import com.example.taskmanagerapi.entity.Task;
import com.example.taskmanagerapi.entity.User;
import com.example.taskmanagerapi.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByOwner(User owner);

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByOwnerAndStatus(User owner, TaskStatus status);

    List<Task> findByTitleContainingIgnoreCase(String keyword);

    List<Task> findByOwnerAndTitleContainingIgnoreCase(User owner, String keyword);
}
