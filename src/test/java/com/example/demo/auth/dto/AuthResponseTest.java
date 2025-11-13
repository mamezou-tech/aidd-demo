package com.example.demo.auth.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    void メッセージのみのコンストラクタ() {
        AuthResponse response = new AuthResponse("登録成功");
        
        assertEquals("登録成功", response.getMessage());
        assertNull(response.getToken());
        assertNull(response.getExpiresIn());
    }

    @Test
    void 全フィールドのコンストラクタ() {
        AuthResponse response = new AuthResponse("ログイン成功", "test-token", 86400000L);
        
        assertEquals("ログイン成功", response.getMessage());
        assertEquals("test-token", response.getToken());
        assertEquals(86400000L, response.getExpiresIn());
    }

    @Test
    void セッターでフィールド設定() {
        AuthResponse response = new AuthResponse();
        response.setMessage("テストメッセージ");
        response.setToken("test-token-123");
        response.setExpiresIn(3600000L);
        
        assertEquals("テストメッセージ", response.getMessage());
        assertEquals("test-token-123", response.getToken());
        assertEquals(3600000L, response.getExpiresIn());
    }
}
