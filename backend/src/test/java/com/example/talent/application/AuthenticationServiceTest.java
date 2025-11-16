package com.example.talent.application;

import com.example.talent.domain.User;
import com.example.talent.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User testUser;
    private String rawPassword = "password123";
    private String hashedPassword = "$2a$10$test";

    @BeforeEach
    void setUp() {
        testUser = new User("admin", hashedPassword);
    }

    @Test
    void ログイン成功() {
        // Given
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);

        // When
        boolean result = authenticationService.authenticate("admin", rawPassword);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void ログイン失敗_パスワード不一致() {
        // Given
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpassword", hashedPassword)).thenReturn(false);

        // When
        boolean result = authenticationService.authenticate("admin", "wrongpassword");

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void ログイン失敗_ユーザー存在しない() {
        // Given
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // When
        boolean result = authenticationService.authenticate("nonexistent", rawPassword);

        // Then
        assertThat(result).isFalse();
    }
}
