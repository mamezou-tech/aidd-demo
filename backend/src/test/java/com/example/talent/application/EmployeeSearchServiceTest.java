package com.example.talent.application;

import com.example.talent.domain.model.Employee;
import com.example.talent.domain.model.SearchCriteria;
import com.example.talent.infrastructure.entity.EmployeeEntity;
import com.example.talent.infrastructure.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeSearchServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeSearchService employeeSearchService;

    @Test
    void shouldFindAllEmployees() {
        EmployeeEntity entity = createEmployeeEntity("E001", "山田太郎", "ORG001");
        when(employeeRepository.findByDeletedFalse()).thenReturn(List.of(entity));

        List<Employee> employees = employeeSearchService.findAll();

        assertThat(employees).hasSize(1);
        assertThat(employees.get(0).getName()).isEqualTo("山田太郎");
    }

    @Test
    void shouldFindEmployeeById() {
        EmployeeEntity entity = createEmployeeEntity("E001", "山田太郎", "ORG001");
        when(employeeRepository.findById("E001")).thenReturn(Optional.of(entity));

        Optional<Employee> employee = employeeSearchService.findById("E001");

        assertThat(employee).isPresent();
        assertThat(employee.get().getEmployeeId()).isEqualTo("E001");
    }

    @Test
    void shouldSearchBySkillsWithAndCondition() {
        List<String> skillIds = List.of("SKILL001", "SKILL002");
        EmployeeEntity entity = createEmployeeEntity("E001", "山田太郎", "ORG001");
        when(employeeRepository.findBySkillsAndDeletedFalse(skillIds, 2L)).thenReturn(List.of(entity));

        SearchCriteria criteria = new SearchCriteria(null, null, null, null, skillIds);
        List<Employee> employees = employeeSearchService.search(criteria);

        assertThat(employees).hasSize(1);
        assertThat(employees.get(0).getEmployeeId()).isEqualTo("E001");
    }

    @Test
    void shouldFindEmployeesByOrganization() {
        EmployeeEntity entity = createEmployeeEntity("E001", "山田太郎", "ORG001");
        when(employeeRepository.findByOrganizationIdAndDeletedFalse("ORG001"))
                .thenReturn(List.of(entity));

        List<Employee> employees = employeeSearchService.findByOrganization("ORG001");

        assertThat(employees).hasSize(1);
        assertThat(employees.get(0).getOrganizationId()).isEqualTo("ORG001");
    }

    @Test
    void shouldSearchEmployeesByName() {
        EmployeeEntity entity = createEmployeeEntity("E001", "山田太郎", "ORG001");
        when(employeeRepository.findByNameContainingAndDeletedFalse("太郎"))
                .thenReturn(List.of(entity));

        List<Employee> employees = employeeSearchService.searchByName("太郎");

        assertThat(employees).hasSize(1);
        assertThat(employees.get(0).getName()).contains("太郎");
    }

    @Test
    void shouldSearchEmployeesByCriteria() {
        EmployeeEntity entity = createEmployeeEntity("E001", "山田太郎", "ORG001");
        when(employeeRepository.findByOrganizationIdAndDeletedFalse("ORG001"))
                .thenReturn(List.of(entity));

        SearchCriteria criteria = new SearchCriteria(null, "ORG001", null, null, null);
        List<Employee> employees = employeeSearchService.search(criteria);

        assertThat(employees).hasSize(1);
        assertThat(employees.get(0).getOrganizationId()).isEqualTo("ORG001");
    }

    private EmployeeEntity createEmployeeEntity(String id, String name, String orgId) {
        return EmployeeEntity.fromDomain(new Employee(
                id, name, orgId, "エンジニア", "正社員",
                LocalDate.of(2020, 4, 1), "test@example.com", "/photos/" + id + ".jpg",
                false, LocalDateTime.now(), LocalDateTime.now()
        ));
    }
}
