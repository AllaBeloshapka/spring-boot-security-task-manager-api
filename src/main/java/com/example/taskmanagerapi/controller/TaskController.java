package com.example.taskmanagerapi.controller;

import com.example.taskmanagerapi.dto.TaskRequest;
import com.example.taskmanagerapi.dto.TaskResponse;
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

/**
 * Controller for task-related endpoints.
 * All endpoints require authentication.
 */
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;
    /**
     * Create a new task for the current user.
     */
    @PostMapping
    public TaskResponse createTask(
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails
    ) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = taskService.createTask(request, user);

        return mapToResponse(task);
    }

    /**
     * Get all tasks for current user.
     */
    @GetMapping
    public List<TaskResponse> getTasks(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
            @RequestParam(required = false) TaskStatus status
    ) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Task> tasks;

        if (status != null) {
            tasks = taskService.getUserTasksByStatus(user, status);
        } else {
            tasks = taskService.getUserTasks(user);
        }

        return tasks.stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Convert Task entity to TaskResponse DTO.
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
