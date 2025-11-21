package com.example.talent.domain.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void ユーザーを作成できる() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User("U001", "test@example.com", "hashedPassword", "テストユーザー", now, now);
        
        assertEquals("U001", user.getUserId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("hashedPassword", user.getPasswordHash());
        assertEquals("テストユーザー", user.getName());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void ユーザーIDがnullの場合は例外() {
        LocalDateTime now = LocalDateTime.now();
        assertThrows(IllegalArgumentException.class, () -> 
            new User(null, "test@example.com", "hashedPassword", "テストユーザー", now, now)
        );
    }

    @Test
    void メールアドレスがnullの場合は例外() {
        LocalDateTime now = LocalDateTime.now();
        assertThrows(IllegalArgumentException.class, () -> 
            new User("U001", null, "hashedPassword", "テストユーザー", now, now)
        );
    }
}
