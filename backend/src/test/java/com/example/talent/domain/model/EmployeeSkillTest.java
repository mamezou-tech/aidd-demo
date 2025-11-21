package com.example.talent.domain.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeSkillTest {

    @Test
    void 社員スキルを作成できる() {
        LocalDateTime now = LocalDateTime.now();
        EmployeeSkill es = new EmployeeSkill("E001", "SK001", 4, now, now);
        
        assertEquals("E001", es.getEmployeeId());
        assertEquals("SK001", es.getSkillId());
        assertEquals(4, es.getLevel());
        assertEquals(now, es.getCreatedAt());
        assertEquals(now, es.getUpdatedAt());
    }

    @Test
    void レベル1から5は有効() {
        LocalDateTime now = LocalDateTime.now();
        assertDoesNotThrow(() -> new EmployeeSkill("E001", "SK001", 1, now, now));
        assertDoesNotThrow(() -> new EmployeeSkill("E001", "SK001", 5, now, now));
    }

    @Test
    void レベル0は無効() {
        LocalDateTime now = LocalDateTime.now();
        assertThrows(IllegalArgumentException.class, () -> 
            new EmployeeSkill("E001", "SK001", 0, now, now)
        );
    }

    @Test
    void レベル6は無効() {
        LocalDateTime now = LocalDateTime.now();
        assertThrows(IllegalArgumentException.class, () -> 
            new EmployeeSkill("E001", "SK001", 6, now, now)
        );
    }
}
