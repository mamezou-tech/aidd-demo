package com.example.talent.domain.model;

import java.time.LocalDateTime;

public final class User {
    private final String userId;
    private final String email;
    private final String passwordHash;
    private final String name;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public User(String userId, String email, String passwordHash, String name, 
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId must not be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email must not be null or blank");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("passwordHash must not be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be null or blank");
        }
        
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getName() { return name; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
