package com.example.talent.application;

import com.example.talent.domain.model.User;
import com.example.talent.infrastructure.entity.UserEntity;
import com.example.talent.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldAuthenticateValidUser() {
        UserEntity userEntity = createUserEntity("U001", "test@example.com", "$2a$10$hash");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches("password", "$2a$10$hash")).thenReturn(true);

        Optional<User> result = authService.authenticate("test@example.com", "password");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void shouldRejectInvalidPassword() {
        UserEntity userEntity = createUserEntity("U001", "test@example.com", "$2a$10$hash");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches("wrong", "$2a$10$hash")).thenReturn(false);

        Optional<User> result = authService.authenticate("test@example.com", "wrong");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldRejectNonExistentUser() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Optional<User> result = authService.authenticate("nonexistent@example.com", "password");

        assertThat(result).isEmpty();
    }

    private UserEntity createUserEntity(String id, String email, String passwordHash) {
        return UserEntity.fromDomain(new User(
                id, email, passwordHash, "Test User",
                LocalDateTime.now(), LocalDateTime.now()
        ));
    }
}
