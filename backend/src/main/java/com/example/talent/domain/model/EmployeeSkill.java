package com.example.talent.domain.model;

import java.time.LocalDateTime;

public final class EmployeeSkill {
    private final String employeeId;
    private final String skillId;
    private final int level;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public EmployeeSkill(String employeeId, String skillId, int level, 
                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (employeeId == null || employeeId.isBlank()) {
            throw new IllegalArgumentException("employeeId must not be null or blank");
        }
        if (skillId == null || skillId.isBlank()) {
            throw new IllegalArgumentException("skillId must not be null or blank");
        }
        if (level < 1 || level > 5) {
            throw new IllegalArgumentException("level must be between 1 and 5");
        }
        
        this.employeeId = employeeId;
        this.skillId = skillId;
        this.level = level;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getEmployeeId() { return employeeId; }
    public String getSkillId() { return skillId; }
    public int getLevel() { return level; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
