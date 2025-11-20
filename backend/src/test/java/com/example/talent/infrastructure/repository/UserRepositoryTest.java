package com.example.talent.infrastructure.repository;

import com.example.talent.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindUserByEmail() {
        Optional<User> user = userRepository.findByEmail("test@example.com")
                .map(entity -> entity.toDomain());
        
        assertThat(user).isPresent();
        assertThat(user.get().getEmail()).isEqualTo("test@example.com");
        assertThat(user.get().getName()).isEqualTo("テストユーザー");
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        Optional<User> user = userRepository.findByEmail("nonexistent@example.com")
                .map(entity -> entity.toDomain());
        
        assertThat(user).isEmpty();
    }

    @Test
    void shouldFindUserById() {
        Optional<User> user = userRepository.findById("U001")
                .map(entity -> entity.toDomain());
        
        assertThat(user).isPresent();
        assertThat(user.get().getUserId()).isEqualTo("U001");
    }
}
