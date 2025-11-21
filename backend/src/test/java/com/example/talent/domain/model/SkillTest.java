package com.example.talent.domain.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class SkillTest {

    @Test
    void スキルを作成できる() {
        LocalDateTime now = LocalDateTime.now();
        Skill skill = new Skill("SK001", "Java", "プログラミング言語", now, now);
        
        assertEquals("SK001", skill.getSkillId());
        assertEquals("Java", skill.getSkillName());
        assertEquals("プログラミング言語", skill.getCategory());
        assertEquals(now, skill.getCreatedAt());
        assertEquals(now, skill.getUpdatedAt());
    }

    @Test
    void スキルIDがnullの場合は例外() {
        LocalDateTime now = LocalDateTime.now();
        assertThrows(IllegalArgumentException.class, () -> 
            new Skill(null, "Java", "プログラミング言語", now, now)
        );
    }
}
