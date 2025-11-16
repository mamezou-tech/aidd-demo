package com.example.talent.repository;

import com.example.talent.TestBase;
import com.example.talent.domain.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeRepositoryTest extends TestBase {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void 氏名で部分一致検索できる() {
        // Given
        Employee employee = new Employee("E001", "田中 太郎", "ﾀﾅｶ ﾀﾛｳ",
                "tanaka@example.com", "課長", "正社員", LocalDate.now());
        employeeRepository.save(employee);

        // When
        Page<Employee> result = employeeRepository.findByFullNameContaining(
                "田中", PageRequest.of(0, 20, Sort.by("fullName")));

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getFullName()).contains("田中");
    }

    @Test
    void カナで部分一致検索できる() {
        // Given
        Employee employee = new Employee("E001", "田中 太郎", "ﾀﾅｶ ﾀﾛｳ",
                "tanaka@example.com", "課長", "正社員", LocalDate.now());
        employeeRepository.save(employee);

        // When
        Page<Employee> result = employeeRepository.findByFullNameKanaContaining(
                "ﾀﾅｶ", PageRequest.of(0, 20, Sort.by("fullName")));

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getFullNameKana()).contains("ﾀﾅｶ");
    }

    @Test
    void 削除済み社員は検索結果に含まれない() {
        // Given
        Employee active = new Employee("E001", "田中 太郎", "ﾀﾅｶ ﾀﾛｳ",
                "tanaka@example.com", "課長", "正社員", LocalDate.now());
        Employee deleted = new Employee("E002", "佐藤 花子", "ｻﾄｳ ﾊﾅｺ",
                "sato@example.com", "主任", "正社員", LocalDate.now());
        deleted.delete();
        
        employeeRepository.save(active);
        employeeRepository.save(deleted);

        // When
        Page<Employee> result = employeeRepository.findByDeletedAtIsNull(
                PageRequest.of(0, 20, Sort.by("fullName")));

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getEmployeeCode()).isEqualTo("E001");
    }

    @Test
    void ページネーションが機能する() {
        // Given
        for (int i = 1; i <= 25; i++) {
            Employee employee = new Employee("E" + String.format("%03d", i),
                    "社員" + i, "ｼｬｲﾝ" + i, "emp" + i + "@example.com",
                    "一般", "正社員", LocalDate.now());
            employeeRepository.save(employee);
        }

        // When
        Page<Employee> page1 = employeeRepository.findByDeletedAtIsNull(
                PageRequest.of(0, 20, Sort.by("fullName")));
        Page<Employee> page2 = employeeRepository.findByDeletedAtIsNull(
                PageRequest.of(1, 20, Sort.by("fullName")));

        // Then
        assertThat(page1.getContent()).hasSize(20);
        assertThat(page2.getContent()).hasSize(5);
        assertThat(page1.getTotalElements()).isEqualTo(25);
    }
}
