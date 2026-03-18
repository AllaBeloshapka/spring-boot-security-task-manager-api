package com.example.taskmanagerapi.service;

import com.example.taskmanagerapi.dto.TaskRequest;
import com.example.taskmanagerapi.entity.Task;
import com.example.taskmanagerapi.entity.User;
import com.example.taskmanagerapi.enums.TaskStatus;
import com.example.taskmanagerapi.exception.AccessDeniedTaskException;
import com.example.taskmanagerapi.exception.TaskNotFoundException;
import com.example.taskmanagerapi.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
     * Creates a new task for a specific user.
     */
    public Task createTask(TaskRequest request, User owner) {

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.NEW);
        task.setOwner(owner);
        task.setCreatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    /**
     * Returns all tasks for a specific user.
     */
    public List<Task> getUserTasks(User user) {
        return taskRepository.findByOwner(user);
    }

    /**
     * Returns task by id with ownership check.
     */
    public Task getTaskById(Long id, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        // Check if user owns this task
        if (!task.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedTaskException("You do not have access to this task");
        }

        return task;
    }

    /**
     * Deletes a task with ownership check.
     */
    public void deleteTask(Long id, User user) {
        Task task = getTaskById(id, user);
        taskRepository.delete(task);
    }

    /**
     * Searches tasks by keyword for a specific user.
     */
    public List<Task> searchTasks(String keyword, User user) {
        return taskRepository.findByOwnerAndTitleContainingIgnoreCase(user, keyword);
    }
    public List<Task> getUserTasksByStatus(User user, TaskStatus status) {
        return taskRepository.findByOwnerAndStatus(user, status);
    }
}