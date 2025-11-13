package com.example.demo.auth.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private static final String TEST_EMAIL = "test@example.com";
    private static final String SECRET = "test-secret-key-for-jwt-token-generation-min-256-bits";
    private static final long EXPIRATION = 3600000; // 1時間

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(SECRET, EXPIRATION);
    }

    @Test
    void generateToken_成功() {
        String token = jwtTokenProvider.generateToken(TEST_EMAIL);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void getEmailFromToken_成功() {
        String token = jwtTokenProvider.generateToken(TEST_EMAIL);
        
        String email = jwtTokenProvider.getEmailFromToken(token);
        
        assertEquals(TEST_EMAIL, email);
    }

    @Test
    void validateToken_有効なトークン() {
        String token = jwtTokenProvider.generateToken(TEST_EMAIL);
        
        boolean isValid = jwtTokenProvider.validateToken(token);
        
        assertTrue(isValid);
    }

    @Test
    void validateToken_無効なトークン() {
        String invalidToken = "invalid.token.here";
        
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);
        
        assertFalse(isValid);
    }

    @Test
    void blacklistToken_ブラックリスト登録後は無効() {
        String token = jwtTokenProvider.generateToken(TEST_EMAIL);
        
        assertTrue(jwtTokenProvider.validateToken(token));
        
        jwtTokenProvider.blacklistToken(token);
        
        assertFalse(jwtTokenProvider.validateToken(token));
    }
}
