package com.example.talent.infrastructure.entity;

import com.example.talent.domain.model.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class UserEntity {
    
    @Id
    @Column(name = "user_id", length = 36)
    private String userId;
    
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;
    
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected UserEntity() {}

    public User toDomain() {
        return new User(userId, email, passwordHash, name, createdAt, updatedAt);
    }

    public static UserEntity fromDomain(User user) {
        UserEntity entity = new UserEntity();
        entity.userId = user.getUserId();
        entity.email = user.getEmail();
        entity.passwordHash = user.getPasswordHash();
        entity.name = user.getName();
        entity.createdAt = user.getCreatedAt();
        entity.updatedAt = user.getUpdatedAt();
        return entity;
    }
}
