package com.example.demo.user.service;

import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String ENCODED_PASSWORD = "encoded_password";

    @Test
    void createUser_成功() {
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail(TEST_EMAIL);
        savedUser.setPassword(ENCODED_PASSWORD);

        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createUser(TEST_EMAIL, TEST_PASSWORD);

        assertNotNull(result);
        assertEquals(TEST_EMAIL, result.getEmail());
        assertEquals(ENCODED_PASSWORD, result.getPassword());
        verify(passwordEncoder).encode(TEST_PASSWORD);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void existsByEmail_存在する() {
        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(true);

        boolean result = userService.existsByEmail(TEST_EMAIL);

        assertTrue(result);
        verify(userRepository).existsByEmail(TEST_EMAIL);
    }

    @Test
    void existsByEmail_存在しない() {
        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);

        boolean result = userService.existsByEmail(TEST_EMAIL);

        assertFalse(result);
        verify(userRepository).existsByEmail(TEST_EMAIL);
    }

    @Test
    void findByEmail_見つかる() {
        User user = new User();
        user.setEmail(TEST_EMAIL);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByEmail(TEST_EMAIL);

        assertTrue(result.isPresent());
        assertEquals(TEST_EMAIL, result.get().getEmail());
        verify(userRepository).findByEmail(TEST_EMAIL);
    }

    @Test
    void findByEmail_見つからない() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        Optional<User> result = userService.findByEmail(TEST_EMAIL);

        assertFalse(result.isPresent());
        verify(userRepository).findByEmail(TEST_EMAIL);
    }
}
