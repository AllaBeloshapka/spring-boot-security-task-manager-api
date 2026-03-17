package com.example.taskmanagerapi.controller;

import com.example.taskmanagerapi.dto.TaskRequest;
import com.example.taskmanagerapi.dto.TaskResponse;
import com.example.taskmanagerapi.entity.Task;
import com.example.taskmanagerapi.entity.User;
import com.example.taskmanagerapi.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for task-related endpoints.
 * All endpoints require authentication.
 */
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * Create a new task for the current user.
     */
    @PostMapping
    public TaskResponse createTask(
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails
    ) {

        User user = new User();
        user.setUsername(userDetails.getUsername());

        Task task = taskService.createTask(request, user);

        return mapToResponse(task);
    }

    /**
     * Get all tasks for current user.
     */
    @GetMapping
    public List<TaskResponse> getTasks(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails
    ) {

        User user = new User();
        user.setUsername(userDetails.getUsername());

        return taskService.getUserTasks(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
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
