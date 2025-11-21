package com.example.talent.domain.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SearchCriteriaTest {

    @Test
    void 検索条件を作成できる() {
        SearchCriteria criteria = new SearchCriteria(
            "山田", "ORG001", "エンジニア", "正社員", List.of("SK001", "SK006")
        );
        
        assertEquals("山田", criteria.getName());
        assertEquals("ORG001", criteria.getOrganizationId());
        assertEquals("エンジニア", criteria.getPosition());
        assertEquals("正社員", criteria.getEmploymentType());
        assertEquals(2, criteria.getSkillIds().size());
    }

    @Test
    void すべてnullでも作成できる() {
        SearchCriteria criteria = new SearchCriteria(null, null, null, null, null);
        
        assertNull(criteria.getName());
        assertNull(criteria.getOrganizationId());
        assertNull(criteria.getPosition());
        assertNull(criteria.getEmploymentType());
        assertNull(criteria.getSkillIds());
    }
}
