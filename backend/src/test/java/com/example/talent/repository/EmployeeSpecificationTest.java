package com.example.talent.repository;

import com.example.talent.TestBase;
import com.example.talent.domain.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeSpecificationTest extends TestBase {

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.save(new Employee("E001", "田中 太郎", "ﾀﾅｶ ﾀﾛｳ",
                "tanaka@example.com", "課長", "正社員", LocalDate.now()));
        employeeRepository.save(new Employee("E002", "佐藤 花子", "ｻﾄｳ ﾊﾅｺ",
                "sato@example.com", "主任", "正社員", LocalDate.now()));
        employeeRepository.save(new Employee("E003", "鈴木 一郎", "ｽｽﾞｷ ｲﾁﾛｳ",
                "suzuki@example.com", "課長", "契約社員", LocalDate.now()));
    }

    @Test
    void 氏名で検索できる() {
        // When
        Specification<Employee> spec = EmployeeSpecification.hasFullName("田中");
        Page<Employee> result = employeeRepository.findAll(spec, 
                PageRequest.of(0, 20, Sort.by("fullName")));

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getFullName()).contains("田中");
    }

    @Test
    void 複数条件でAND検索できる() {
        // When
        Specification<Employee> spec = Specification
                .where(EmployeeSpecification.hasPosition("課長"))
                .and(EmployeeSpecification.hasEmploymentType("正社員"))
                .and(EmployeeSpecification.isNotDeleted());
        
        Page<Employee> result = employeeRepository.findAll(spec,
                PageRequest.of(0, 20, Sort.by("fullName")));

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getEmployeeCode()).isEqualTo("E001");
    }

    @Test
    void 削除済みを除外できる() {
        // Given
        Employee deleted = employeeRepository.findAll().get(0);
        deleted.delete();
        employeeRepository.save(deleted);

        // When
        Specification<Employee> spec = EmployeeSpecification.isNotDeleted();
        Page<Employee> result = employeeRepository.findAll(spec,
                PageRequest.of(0, 20, Sort.by("fullName")));

        // Then
        assertThat(result.getContent()).hasSize(2);
    }
}
