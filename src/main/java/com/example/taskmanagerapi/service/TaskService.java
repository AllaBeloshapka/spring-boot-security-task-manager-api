package com.example.taskmanagerapi.service;

import com.example.taskmanagerapi.dto.TaskRequest;
import com.example.taskmanagerapi.dto.TaskUpdateRequest;
import com.example.taskmanagerapi.entity.Task;
import com.example.taskmanagerapi.entity.User;
import com.example.taskmanagerapi.enums.TaskStatus;
import com.example.taskmanagerapi.exception.AccessDeniedTaskException;
import com.example.taskmanagerapi.exception.TaskNotFoundException;
import com.example.taskmanagerapi.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for task-related operations.
 * Handles business logic and ownership control.
 */
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    /**
     * Создать задачу
     */
    public Task createTask(TaskRequest request) {

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.NEW);

        return taskRepository.save(task);
    }

    /**
     * Получить все задачи
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Получить задачу по id
     */
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    /**
     * Обновить задачу
     */
    public Task updateTask(Long id, TaskUpdateRequest request) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }

        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        return taskRepository.save(task);
    }

    /**
     * Удалить задачу
     */
    public void deleteTask(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskRepository.delete(task);
    }

    /**
     * Поиск по ключевому слову
     */
    public List<Task> searchTasks(String keyword) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

    /**
     * Фильтр по статусу
     */
    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getStatus() == status)
                .toList();
    }
}