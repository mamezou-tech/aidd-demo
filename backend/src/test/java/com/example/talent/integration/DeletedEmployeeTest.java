package com.example.talent.integration;

import com.example.talent.infrastructure.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DeletedEmployeeTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void shouldExcludeDeletedEmployeesFromSearch() {
        var result = employeeRepository.findByDeletedFalse();
        assertNotNull(result);
        assertTrue(result.stream()
                .map(e -> e.toDomain())
                .noneMatch(e -> e.isDeleted()));
    }

    @Test
    void shouldFindNonDeletedEmployeeById() {
        var employee = employeeRepository.findById("E001");
        assertTrue(employee.isPresent());
        assertFalse(employee.get().toDomain().isDeleted());
    }
}
