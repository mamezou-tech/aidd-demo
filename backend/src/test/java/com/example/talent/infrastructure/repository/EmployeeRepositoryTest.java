package com.example.talent.infrastructure.repository;

import com.example.talent.domain.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void shouldFindAllNonDeletedEmployees() {
        List<Employee> employees = employeeRepository.findByDeletedFalse().stream()
                .map(entity -> entity.toDomain())
                .collect(Collectors.toList());
        
        assertThat(employees).hasSize(100);
    }

    @Test
    void shouldFindEmployeeById() {
        Optional<Employee> employee = employeeRepository.findById("E001")
                .map(entity -> entity.toDomain());
        
        assertThat(employee).isPresent();
        assertThat(employee.get().getName()).isEqualTo("山田太郎");
    }

    @Test
    void shouldFindEmployeesByOrganization() {
        List<Employee> employees = employeeRepository.findByOrganizationIdAndDeletedFalse("ORG001").stream()
                .map(entity -> entity.toDomain())
                .collect(Collectors.toList());
        
        assertThat(employees).isNotEmpty();
        assertThat(employees).allMatch(e -> e.getOrganizationId().equals("ORG001"));
    }

    @Test
    void shouldFindEmployeesByNameContaining() {
        List<Employee> employees = employeeRepository.findByNameContainingAndDeletedFalse("太郎").stream()
                .map(entity -> entity.toDomain())
                .collect(Collectors.toList());
        
        assertThat(employees).isNotEmpty();
        assertThat(employees).allMatch(e -> e.getName().contains("太郎"));
    }
}
