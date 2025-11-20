package com.example.talent.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class Employee {
    private final String employeeId;
    private final String name;
    private final String organizationId;
    private final String position;
    private final String employmentType;
    private final LocalDate hireDate;
    private final String email;
    private final String photoPath;
    private final boolean deleted;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Employee(String employeeId, String name, String organizationId, String position,
                   String employmentType, LocalDate hireDate, String email, String photoPath,
                   boolean deleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (employeeId == null || employeeId.isBlank()) {
            throw new IllegalArgumentException("employeeId must not be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be null or blank");
        }
        if (organizationId == null || organizationId.isBlank()) {
            throw new IllegalArgumentException("organizationId must not be null or blank");
        }
        if (position == null || position.isBlank()) {
            throw new IllegalArgumentException("position must not be null or blank");
        }
        if (employmentType == null || employmentType.isBlank()) {
            throw new IllegalArgumentException("employmentType must not be null or blank");
        }
        if (hireDate == null) {
            throw new IllegalArgumentException("hireDate must not be null");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email must not be null or blank");
        }
        
        this.employeeId = employeeId;
        this.name = name;
        this.organizationId = organizationId;
        this.position = position;
        this.employmentType = employmentType;
        this.hireDate = hireDate;
        this.email = email;
        this.photoPath = photoPath;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public String getOrganizationId() { return organizationId; }
    public String getPosition() { return position; }
    public String getEmploymentType() { return employmentType; }
    public LocalDate getHireDate() { return hireDate; }
    public String getEmail() { return email; }
    public String getPhotoPath() { return photoPath; }
    public boolean isDeleted() { return deleted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
