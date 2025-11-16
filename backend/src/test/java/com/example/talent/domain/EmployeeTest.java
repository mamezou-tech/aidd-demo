package com.example.talent.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeTest {

    @Test
    void Employeeエンティティを生成できる() {
        // Given
        String employeeCode = "E001";
        String fullName = "John Smith";
        String fullNameKana = "ｼﾞｮﾝ ｽﾐｽ";
        String email = "john.smith@example.com";
        String position = "部長";
        String employmentType = "正社員";
        LocalDate hireDate = LocalDate.of(2020, 4, 1);

        // When
        Employee employee = new Employee(
            employeeCode, fullName, fullNameKana, 
            email, position, employmentType, hireDate
        );

        // Then
        assertThat(employee.getEmployeeCode()).isEqualTo(employeeCode);
        assertThat(employee.getFullName()).isEqualTo(fullName);
        assertThat(employee.getFullNameKana()).isEqualTo(fullNameKana);
        assertThat(employee.getEmail()).isEqualTo(email);
        assertThat(employee.getPosition()).isEqualTo(position);
        assertThat(employee.getEmploymentType()).isEqualTo(employmentType);
        assertThat(employee.getHireDate()).isEqualTo(hireDate);
        assertThat(employee.getDeletedAt()).isNull();
    }

    @Test
    void 論理削除できる() {
        // Given
        Employee employee = new Employee(
            "E001", "John Smith", "ｼﾞｮﾝ ｽﾐｽ",
            "john@example.com", "部長", "正社員", LocalDate.now()
        );

        // When
        employee.delete();

        // Then
        assertThat(employee.getDeletedAt()).isNotNull();
    }
}
