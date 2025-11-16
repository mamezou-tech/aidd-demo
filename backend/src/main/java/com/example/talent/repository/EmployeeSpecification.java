package com.example.talent.repository;

import com.example.talent.domain.Employee;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

    public static Specification<Employee> hasFullName(String fullName) {
        return (root, query, cb) -> 
            fullName == null || fullName.isBlank() ? 
                cb.conjunction() : 
                cb.like(root.get("fullName"), "%" + fullName + "%");
    }

    public static Specification<Employee> hasFullNameKana(String fullNameKana) {
        return (root, query, cb) -> 
            fullNameKana == null || fullNameKana.isBlank() ? 
                cb.conjunction() : 
                cb.like(root.get("fullNameKana"), "%" + fullNameKana + "%");
    }

    public static Specification<Employee> hasEmployeeCode(String employeeCode) {
        return (root, query, cb) -> 
            employeeCode == null || employeeCode.isBlank() ? 
                cb.conjunction() : 
                cb.like(root.get("employeeCode"), "%" + employeeCode + "%");
    }

    public static Specification<Employee> hasEmail(String email) {
        return (root, query, cb) -> 
            email == null || email.isBlank() ? 
                cb.conjunction() : 
                cb.like(root.get("email"), "%" + email + "%");
    }

    public static Specification<Employee> hasPosition(String position) {
        return (root, query, cb) -> 
            position == null || position.isBlank() ? 
                cb.conjunction() : 
                cb.like(root.get("position"), "%" + position + "%");
    }

    public static Specification<Employee> hasEmploymentType(String employmentType) {
        return (root, query, cb) -> 
            employmentType == null || employmentType.isBlank() ? 
                cb.conjunction() : 
                cb.equal(root.get("employmentType"), employmentType);
    }

    public static Specification<Employee> isNotDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }
}
