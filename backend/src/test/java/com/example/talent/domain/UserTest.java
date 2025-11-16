package com.example.talent.domain;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void パスワードをハッシュ化できる() {
        // Given
        String rawPassword = "password123";
        
        // When
        String hashedPassword = passwordEncoder.encode(rawPassword);
        
        // Then
        assertThat(hashedPassword).isNotEqualTo(rawPassword);
        assertThat(passwordEncoder.matches(rawPassword, hashedPassword)).isTrue();
    }

    @Test
    void Userエンティティを生成できる() {
        // Given
        String username = "admin";
        String passwordHash = passwordEncoder.encode("password123");
        
        // When
        User user = new User(username, passwordHash);
        
        // Then
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getPasswordHash()).isEqualTo(passwordHash);
    }
}
