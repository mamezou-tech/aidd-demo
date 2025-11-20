package com.example.talent.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil("test-secret-key-that-is-long-enough-for-hmac-sha256");

    @Test
    void shouldGenerateToken() {
        String token = jwtUtil.generateToken("test@example.com");
        
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    void shouldExtractEmail() {
        String token = jwtUtil.generateToken("test@example.com");
        String email = jwtUtil.extractEmail(token);
        
        assertThat(email).isEqualTo("test@example.com");
    }

    @Test
    void shouldValidateToken() {
        String token = jwtUtil.generateToken("test@example.com");
        
        assertThat(jwtUtil.validateToken(token, "test@example.com")).isTrue();
        assertThat(jwtUtil.validateToken(token, "other@example.com")).isFalse();
    }

    @Test
    void shouldValidateTokenWithCorrectEmail() {
        String token = jwtUtil.generateToken("test@example.com");
        assertThat(jwtUtil.validateToken(token, "test@example.com")).isTrue();
    }
}
