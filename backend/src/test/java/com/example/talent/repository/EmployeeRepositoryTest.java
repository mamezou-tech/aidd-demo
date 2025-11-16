package com.example.talent.repository;

import com.example.talent.TestBase;
import com.example.talent.domain.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class EmployeeRepositoryTest extends TestBase {

    @Autowired
    private EmployeeRepository employeeRepository;

    private String uniqueEmail() {
        return UUID.randomUUID().toString() + "@example.com";
    }

    @Test
    void 氏名で部分一致検索できる() {
        Employee employee = new Employee("E" + UUID.randomUUID().toString().substring(0, 8), "田中 太郎", "ﾀﾅｶ ﾀﾛｳ",
                uniqueEmail(), "課長", "正社員", LocalDate.now());
        employeeRepository.saveAndFlush(employee);

        Page<Employee> result = employeeRepository.findByFullNameContaining(
                "田中", PageRequest.of(0, 20, Sort.by("fullName")));

        assertThat(result.getContent()).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void 削除済み社員は検索結果に含まれない() {
        String activeCode = "E" + UUID.randomUUID().toString().substring(0, 8);
        String deletedCode = "E" + UUID.randomUUID().toString().substring(0, 8);
        
        Employee active = new Employee(activeCode, "田中 太郎", "ﾀﾅｶ ﾀﾛｳ",
                uniqueEmail(), "課長", "正社員", LocalDate.now());
        Employee deleted = new Employee(deletedCode, "佐藤 花子", "ｻﾄｳ ﾊﾅｺ",
                uniqueEmail(), "主任", "正社員", LocalDate.now());
        deleted.delete();
        
        employeeRepository.save(active);
        employeeRepository.save(deleted);
        employeeRepository.flush();

        Page<Employee> result = employeeRepository.findByDeletedAtIsNull(
                PageRequest.of(0, 20, Sort.by("fullName")));

        assertThat(result.getContent().stream()
                .noneMatch(e -> e.getEmployeeCode().equals(deletedCode))).isTrue();
    }
}
