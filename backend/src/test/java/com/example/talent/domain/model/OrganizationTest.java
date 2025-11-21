package com.example.talent.domain.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class OrganizationTest {

    @Test
    void 組織を作成できる() {
        LocalDateTime now = LocalDateTime.now();
        Organization org = new Organization("ORG001", "開発部", now, now);
        
        assertEquals("ORG001", org.getOrganizationId());
        assertEquals("開発部", org.getOrganizationName());
        assertEquals(now, org.getCreatedAt());
        assertEquals(now, org.getUpdatedAt());
    }

    @Test
    void 組織IDがnullの場合は例外() {
        LocalDateTime now = LocalDateTime.now();
        assertThrows(IllegalArgumentException.class, () -> 
            new Organization(null, "開発部", now, now)
        );
    }
}
