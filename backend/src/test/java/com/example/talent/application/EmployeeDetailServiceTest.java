package com.example.talent.application;

import com.example.talent.domain.model.Employee;
import com.example.talent.infrastructure.entity.EmployeeEntity;
import com.example.talent.infrastructure.repository.EmployeeRepository;
import com.example.talent.infrastructure.repository.EmployeeSkillRepository;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeDetailServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeSkillRepository employeeSkillRepository;

    @InjectMocks
    private EmployeeDetailService employeeDetailService;

    @Test
    void shouldGetEmployeeDetail() {
        EmployeeEntity entity = createEmployeeEntity("E001", "山田太郎", "ORG001", false);
        when(employeeRepository.findById("E001")).thenReturn(Optional.of(entity));
        when(employeeSkillRepository.findByEmployeeId("E001")).thenReturn(List.of());

        EmployeeDetailDTO employee = employeeDetailService.getDetail("E001");

        assertThat(employee.getEmployeeId()).isEqualTo("E001");
        assertThat(employee.getName()).isEqualTo("山田太郎");
    }

    @Test
    void shouldThrowExceptionWhenEmployeeNotFound() {
        when(employeeRepository.findById("E999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeDetailService.getDetail("E999"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Employee not found");
    }

    @Test
    void shouldThrowExceptionWhenEmployeeDeleted() {
        EmployeeEntity entity = createEmployeeEntity("E001", "山田太郎", "ORG001", true);
        when(employeeRepository.findById("E001")).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> employeeDetailService.getDetail("E001"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Employee is deleted");
    }

    private EmployeeEntity createEmployeeEntity(String id, String name, String orgId, boolean deleted) {
        return EmployeeEntity.fromDomain(new Employee(
                id, name, orgId, "エンジニア", "正社員",
                LocalDate.of(2020, 4, 1), "test@example.com", "/photos/" + id + ".jpg",
                deleted, LocalDateTime.now(), LocalDateTime.now()
        ));
    }
}
