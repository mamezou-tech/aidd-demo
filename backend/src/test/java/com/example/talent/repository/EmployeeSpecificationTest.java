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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class EmployeeSpecificationTest extends TestBase {

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.save(new Employee("E" + UUID.randomUUID().toString().substring(0, 8), "田中 太郎", "ﾀﾅｶ ﾀﾛｳ",
                UUID.randomUUID() + "@example.com", "課長", "正社員", LocalDate.now()));
        employeeRepository.save(new Employee("E" + UUID.randomUUID().toString().substring(0, 8), "佐藤 花子", "ｻﾄｳ ﾊﾅｺ",
                UUID.randomUUID() + "@example.com", "主任", "正社員", LocalDate.now()));
        employeeRepository.save(new Employee("E" + UUID.randomUUID().toString().substring(0, 8), "鈴木 一郎", "ｽｽﾞｷ ｲﾁﾛｳ",
                UUID.randomUUID() + "@example.com", "課長", "契約社員", LocalDate.now()));
    }

    @Test
    void 氏名で検索できる() {
        // When
        Specification<Employee> spec = EmployeeSpecification.hasFullName("田中");
        Page<Employee> result = employeeRepository.findAll(spec, 
                PageRequest.of(0, 20, Sort.by("fullName")));

        // Then
        assertThat(result.getContent()).hasSizeGreaterThanOrEqualTo(1);
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
        assertThat(result.getContent()).hasSizeGreaterThanOrEqualTo(1);
        assertThat(result.getContent().stream()
                .anyMatch(e -> e.getFullName().contains("田中"))).isTrue();
    }

    @Test
    void 削除済みを除外できる() {
        // Given
        long beforeCount = employeeRepository.count();
        Employee deleted = employeeRepository.findAll().get(0);
        deleted.delete();
        employeeRepository.save(deleted);

        // When
        Specification<Employee> spec = EmployeeSpecification.isNotDeleted();
        Page<Employee> result = employeeRepository.findAll(spec,
                PageRequest.of(0, 20, Sort.by("fullName")));

        // Then
        assertThat(result.getTotalElements()).isEqualTo(beforeCount - 1);
    }
}
