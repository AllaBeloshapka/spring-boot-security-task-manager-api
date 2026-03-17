package com.example.taskmanagerapi.entity;
// Package containing entity classes — objects mapped to database tables

import jakarta.persistence.*;
// JPA annotations for mapping classes to database tables

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// Lombok — automatically generates getters, setters, and constructors

import java.time.LocalDateTime;
// Class for storing date and time

import java.util.ArrayList;
import java.util.List;
// List collection for storing user's tasks

@Entity
// This class is a JPA entity (represents a database table)

@Table(name = "users")
// Name of the table in the database

@Data
// Lombok generates:
// getters
// setters
// toString
// equals
// hashCode

@NoArgsConstructor
// No-args constructor: new User()

@AllArgsConstructor
// All-args constructor

public class User {

    @Id
    // Primary key of the table

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Database automatically generates id values (1,2,3,...)

    private Long id;

    /**
     * Unique username of the user
     */

    @Column(unique = true, nullable = false)
    // unique = true → username must be unique
    // nullable = false → field is required

    private String username;

    /**
     * Hashed password (BCrypt)
     * NEVER store plain-text passwords!
     */

    @Column(nullable = false)
    // Password is required

    private String password;
    // This is NOT the real password,
    // but its hashed version

    /**
     * User role: USER or ADMIN
     * ROLE_ prefix is required for Spring Security
     */

    @Column(nullable = false)
    // Role is required

    private String role = "ROLE_USER";
    // By default, a new user gets USER role

    /**
     * Whether the account is active
     */

    @Column(nullable = false)
    // Field is required

    private Boolean enabled = true;
    // true = account is active
    // false = account is disabled

    /**
     * User's tasks
     */

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    // Relationship: one user → many tasks

    // mappedBy = "owner"
    // means the relationship is controlled by the "owner" field in Task class

    // cascade = ALL
    // if user is deleted → all their tasks are also deleted

    // orphanRemoval = true
    // if a task is removed from the user's task list → it is deleted from DB

    private List<Task> tasks = new ArrayList<>();
    // List of user's tasks
    // Initialized to avoid null

    @Column(nullable = false, updatable = false)
    // createdAt is required
    // and cannot be updated after creation

    private LocalDateTime createdAt;
    // Timestamp of user creation

    @PrePersist
    // This method is called automatically before saving to DB

    protected void onCreate() {
        createdAt = LocalDateTime.now();
        // When a user is created — set current timestamp automatically
    }
}