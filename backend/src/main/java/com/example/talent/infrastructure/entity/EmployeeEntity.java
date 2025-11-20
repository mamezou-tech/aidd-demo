package com.example.talent.infrastructure.entity;

import com.example.talent.domain.model.Employee;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
public class EmployeeEntity {
    
    @Id
    @Column(name = "employee_id", length = 36)
    private String employeeId;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "organization_id", nullable = false, length = 36)
    private String organizationId;
    
    @Column(name = "position", nullable = false, length = 100)
    private String position;
    
    @Column(name = "employment_type", nullable = false, length = 50)
    private String employmentType;
    
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;
    
    @Column(name = "email", nullable = false, length = 255)
    private String email;
    
    @Column(name = "photo_path", length = 255)
    private String photoPath;
    
    @Column(name = "deleted", nullable = false)
    private boolean deleted;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected EmployeeEntity() {}

    public Employee toDomain() {
        return new Employee(employeeId, name, organizationId, position, employmentType,
                          hireDate, email, photoPath, deleted, createdAt, updatedAt);
    }

    public static EmployeeEntity fromDomain(Employee emp) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.employeeId = emp.getEmployeeId();
        entity.name = emp.getName();
        entity.organizationId = emp.getOrganizationId();
        entity.position = emp.getPosition();
        entity.employmentType = emp.getEmploymentType();
        entity.hireDate = emp.getHireDate();
        entity.email = emp.getEmail();
        entity.photoPath = emp.getPhotoPath();
        entity.deleted = emp.isDeleted();
        entity.createdAt = emp.getCreatedAt();
        entity.updatedAt = emp.getUpdatedAt();
        return entity;
    }
}
