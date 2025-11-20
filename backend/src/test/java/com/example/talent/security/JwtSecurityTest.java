package com.example.talent.security;

import com.example.talent.config.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtSecurityTest {

    private final JwtUtil jwtUtil = new JwtUtil("test-secret-key-that-is-long-enough-for-hmac-sha256-algorithm");

    @Test
    void shouldGenerateValidToken() {
        String token = jwtUtil.generateToken("test@example.com");
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void shouldExtractEmailFromToken() {
        String token = jwtUtil.generateToken("test@example.com");
        String email = jwtUtil.extractEmail(token);
        assertEquals("test@example.com", email);
    }

    @Test
    void shouldValidateValidToken() {
        String token = jwtUtil.generateToken("test@example.com");
        assertTrue(jwtUtil.validateToken(token, "test@example.com"));
    }

    @Test
    void shouldRejectInvalidToken() {
        assertThrows(JwtException.class, () -> {
            jwtUtil.extractEmail("invalid-token");
        });
    }
}
