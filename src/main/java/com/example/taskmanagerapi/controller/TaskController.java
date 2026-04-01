package com.example.taskmanagerapi.controller;

import com.example.taskmanagerapi.dto.TaskRequest;
import com.example.taskmanagerapi.dto.TaskResponse;
import com.example.taskmanagerapi.dto.TaskUpdateRequest;
import com.example.taskmanagerapi.entity.Task;
import com.example.taskmanagerapi.entity.User;
import com.example.taskmanagerapi.enums.TaskStatus;
import com.example.taskmanagerapi.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.taskmanagerapi.repository.UserRepository;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * Создать задачу
     */
    @PostMapping
    public TaskResponse createTask(@RequestBody TaskRequest request) {

        Task task = taskService.createTask(request);

        return mapToResponse(task);
    }

    /**
     * Обновить задачу
     */
    @PutMapping("/{id}")
    public TaskResponse updateTask(
            @PathVariable Long id,
            @RequestBody TaskUpdateRequest request
    ) {

        Task updatedTask = taskService.updateTask(id, request);

        return mapToResponse(updatedTask);
    }

    /**
     * Удалить задачу
     */
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    /**
     * Получить все задачи
     */
    @GetMapping
    public List<TaskResponse> getTasks() {

        List<Task> tasks = taskService.getAllTasks();

        return tasks.stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Преобразование в DTO
     */
    private TaskResponse mapToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setCreatedAt(task.getCreatedAt());
        return response;
    }
}