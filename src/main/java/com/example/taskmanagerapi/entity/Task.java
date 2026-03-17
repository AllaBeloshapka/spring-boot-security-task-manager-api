package com.example.taskmanagerapi.entity;
// Package containing entity classes (mapped to database tables)

import com.example.taskmanagerapi.enums.TaskStatus;
// Import enum with task statuses (TODO, IN_PROGRESS, DONE, etc.)

import jakarta.persistence.*;
// JPA annotations for database mapping

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// Lombok automatically generates constructors, getters, setters, etc.

import java.time.LocalDateTime;
// Class for working with date and time

import java.util.ArrayList;
import java.util.List;
// These imports are currently unused (can be removed)

@Entity
// Marks this class as a JPA entity (stored in a database table)

@Table(name = "tasks")
// Name of the table in the database

@Data
// Lombok generates:
// getters
// setters
// toString
// equals
// hashCode

@NoArgsConstructor
// No-args constructor: new Task()

@AllArgsConstructor
// All-args constructor

public class Task {

    @Id
    // Primary key of the table

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Database automatically generates id values (1,2,3,...)

    private Long id;

    @Column(nullable = false)
    // Field is required
    // Task cannot be saved without a title

    private String title;

    @Column(length = 1000)
    // Maximum length of description is 1000 characters

    private String description;

    @Enumerated(EnumType.STRING)
    // Store enum as a string in the database
    // Example: TODO, IN_PROGRESS, DONE

    private TaskStatus status = TaskStatus.TODO;
    // By default, a new task has status TODO

    /**
     * Task owner
     * Important for access control!
     */

    @ManyToOne(fetch = FetchType.LAZY)
    // Relationship: many tasks -> one user
    // One user can have multiple tasks

    // LAZY means:
    // user is loaded from DB only when actually needed

    @JoinColumn(name = "owner_id", nullable = false)
    // Column owner_id in tasks table
    // Stores the user id

    private User owner;
    // The user who owns the task

    @Column(nullable = false, updatable = false)
    // createdAt is required
    // and cannot be updated after creation

    private LocalDateTime createdAt;
    // Timestamp of task creation

    @PrePersist
    // This method is called automatically before saving to DB

    protected void onCreate() {
        createdAt = LocalDateTime.now();
        // Set current time when task is created
    }
}
