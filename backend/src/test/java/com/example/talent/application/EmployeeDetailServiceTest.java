package com.example.talent.application;

import com.example.talent.domain.Employee;
import com.example.talent.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeDetailServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeDetailService employeeDetailService;

    @Test
    void IDで社員を取得できる() {
        // Given
        Employee employee = new Employee("E001", "田中 太郎", "ﾀﾅｶ ﾀﾛｳ",
                "tanaka@example.com", "課長", "正社員", LocalDate.now());
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // When
        Employee result = employeeDetailService.findById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFullName()).isEqualTo("田中 太郎");
    }

    @Test
    void 存在しない社員IDで例外が発生する() {
        // Given
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> employeeDetailService.findById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("社員が見つかりません");
    }

    @Test
    void 削除済み社員で例外が発生する() {
        // Given
        Employee deletedEmployee = new Employee("E001", "田中 太郎", "ﾀﾅｶ ﾀﾛｳ",
                "tanaka@example.com", "課長", "正社員", LocalDate.now());
        deletedEmployee.delete();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(deletedEmployee));

        // When & Then
        assertThatThrownBy(() -> employeeDetailService.findById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("社員が見つかりません");
    }
}
