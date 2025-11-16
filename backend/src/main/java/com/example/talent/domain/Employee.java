package com.example.talent.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(nullable = false, unique = true, length = 20)
    private String employeeCode;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 100)
    private String fullNameKana;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 50)
    private String position;

    @Column(nullable = false, length = 20)
    private String employmentType;

    @Column(nullable = false)
    private LocalDate hireDate;

    @Column
    private LocalDateTime deletedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected Employee() {
    }

    public Employee(String employeeCode, String fullName, String fullNameKana,
                    String email, String position, String employmentType, LocalDate hireDate) {
        this.employeeCode = employeeCode;
        this.fullName = fullName;
        this.fullNameKana = fullNameKana;
        this.email = email;
        this.position = position;
        this.employmentType = employmentType;
        this.hireDate = hireDate;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    // Getters
    public Long getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public String getFullName() {
        return fullName;
    }

    public String getFullNameKana() {
        return fullNameKana;
    }

    public String getEmail() {
        return email;
    }

    public String getPosition() {
        return position;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
}
