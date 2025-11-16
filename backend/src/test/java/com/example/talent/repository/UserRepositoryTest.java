package com.example.talent.repository;

import com.example.talent.TestBase;
import com.example.talent.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryTest extends TestBase {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void usernameでユーザーを検索できる() {
        // Given
        String username = "testuser";
        String passwordHash = passwordEncoder.encode("password");
        User user = new User(username, passwordHash);
        userRepository.save(user);

        // When
        Optional<User> found = userRepository.findByUsername(username);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo(username);
    }

    @Test
    void 存在しないusernameで検索すると空を返す() {
        // When
        Optional<User> found = userRepository.findByUsername("nonexistent");

        // Then
        assertThat(found).isEmpty();
    }
}
