package com.example.talent.domain.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void 社員を作成できる() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate hireDate = LocalDate.of(2020, 4, 1);
        
        Employee employee = new Employee(
            "E001", "山田太郎", "ORG001", "エンジニア", "正社員",
            hireDate, "yamada@example.com", "/photos/E001.jpg", false, now, now
        );
        
        assertEquals("E001", employee.getEmployeeId());
        assertEquals("山田太郎", employee.getName());
        assertEquals("ORG001", employee.getOrganizationId());
        assertEquals("エンジニア", employee.getPosition());
        assertEquals("正社員", employee.getEmploymentType());
        assertEquals(hireDate, employee.getHireDate());
        assertEquals("yamada@example.com", employee.getEmail());
        assertEquals("/photos/E001.jpg", employee.getPhotoPath());
        assertFalse(employee.isDeleted());
    }

    @Test
    void 社員IDがnullの場合は例外() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate hireDate = LocalDate.of(2020, 4, 1);
        
        assertThrows(IllegalArgumentException.class, () -> 
            new Employee(null, "山田太郎", "ORG001", "エンジニア", "正社員",
                hireDate, "yamada@example.com", null, false, now, now)
        );
    }
}
