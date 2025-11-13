package com.example.demo.auth.service;

import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.SignupRequest;
import com.example.demo.auth.security.JwtTokenProvider;
import com.example.demo.common.exception.AuthException;
import com.example.demo.user.entity.User;
import com.example.demo.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String ENCODED_PASSWORD = "encoded_password";
    private static final String TEST_TOKEN = "test.jwt.token";
    private static final long JWT_EXPIRATION = 86400000L;

    @Test
    void signup_成功() {
        SignupRequest request = new SignupRequest(TEST_EMAIL, TEST_PASSWORD);
        User user = new User();
        user.setId(1L);
        user.setEmail(TEST_EMAIL);

        when(userService.existsByEmail(TEST_EMAIL)).thenReturn(false);
        when(userService.createUser(TEST_EMAIL, TEST_PASSWORD)).thenReturn(user);

        AuthResponse response = authService.signup(request);

        assertNotNull(response);
        assertEquals("ユーザー登録が完了しました", response.getMessage());
        verify(userService).existsByEmail(TEST_EMAIL);
        verify(userService).createUser(TEST_EMAIL, TEST_PASSWORD);
    }

    @Test
    void signup_メールアドレス重複() {
        SignupRequest request = new SignupRequest(TEST_EMAIL, TEST_PASSWORD);

        when(userService.existsByEmail(TEST_EMAIL)).thenReturn(true);

        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.signup(request);
        });

        assertEquals("このメールアドレスは既に登録されています", exception.getMessage());
        verify(userService).existsByEmail(TEST_EMAIL);
        verify(userService, never()).createUser(anyString(), anyString());
    }

    @Test
    void login_成功() {
        ReflectionTestUtils.setField(authService, "jwtExpiration", JWT_EXPIRATION);
        
        LoginRequest request = new LoginRequest(TEST_EMAIL, TEST_PASSWORD);
        User user = new User();
        user.setEmail(TEST_EMAIL);
        user.setPassword(ENCODED_PASSWORD);

        when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(TEST_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        when(jwtTokenProvider.generateToken(TEST_EMAIL)).thenReturn(TEST_TOKEN);

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("ログインに成功しました", response.getMessage());
        assertEquals(TEST_TOKEN, response.getToken());
        assertEquals(JWT_EXPIRATION, response.getExpiresIn());
        verify(userService).findByEmail(TEST_EMAIL);
        verify(passwordEncoder).matches(TEST_PASSWORD, ENCODED_PASSWORD);
        verify(jwtTokenProvider).generateToken(TEST_EMAIL);
    }

    @Test
    void login_ユーザーが存在しない() {
        LoginRequest request = new LoginRequest(TEST_EMAIL, TEST_PASSWORD);

        when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.login(request);
        });

        assertEquals("メールアドレスまたはパスワードが正しくありません", exception.getMessage());
        verify(userService).findByEmail(TEST_EMAIL);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void login_パスワードが一致しない() {
        LoginRequest request = new LoginRequest(TEST_EMAIL, TEST_PASSWORD);
        User user = new User();
        user.setEmail(TEST_EMAIL);
        user.setPassword(ENCODED_PASSWORD);

        when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(TEST_PASSWORD, ENCODED_PASSWORD)).thenReturn(false);

        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.login(request);
        });

        assertEquals("メールアドレスまたはパスワードが正しくありません", exception.getMessage());
        verify(userService).findByEmail(TEST_EMAIL);
        verify(passwordEncoder).matches(TEST_PASSWORD, ENCODED_PASSWORD);
        verify(jwtTokenProvider, never()).generateToken(anyString());
    }
}
