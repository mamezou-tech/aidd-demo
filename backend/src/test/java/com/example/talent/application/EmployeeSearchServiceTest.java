package com.example.talent.application;

import com.example.talent.domain.Employee;
import com.example.talent.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeSearchServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeSearchService employeeSearchService;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        employee1 = new Employee("E001", "田中 太郎", "ﾀﾅｶ ﾀﾛｳ",
                "tanaka@example.com", "課長", "正社員", LocalDate.now());
        employee2 = new Employee("E002", "佐藤 花子", "ｻﾄｳ ﾊﾅｺ",
                "sato@example.com", "主任", "正社員", LocalDate.now());
    }

    @Test
    void 検索条件なしで全社員を取得できる() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by("fullName"));
        Page<Employee> page = new PageImpl<>(List.of(employee1, employee2));
        when(employeeRepository.findAll(any(Specification.class), eq(pageRequest))).thenReturn(page);

        // When
        Page<Employee> result = employeeSearchService.search(null, null, null, null, null, null, pageRequest);

        // Then
        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    void 氏名で検索できる() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by("fullName"));
        Page<Employee> page = new PageImpl<>(List.of(employee1));
        when(employeeRepository.findAll(any(Specification.class), eq(pageRequest))).thenReturn(page);

        // When
        Page<Employee> result = employeeSearchService.search("田中", null, null, null, null, null, pageRequest);

        // Then
        assertThat(result.getContent()).hasSize(1);
    }
}
