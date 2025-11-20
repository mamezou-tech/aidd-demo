package com.example.talent.domain.model;

import java.time.LocalDateTime;

public final class Skill {
    private final String skillId;
    private final String skillName;
    private final String category;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Skill(String skillId, String skillName, String category, 
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (skillId == null || skillId.isBlank()) {
            throw new IllegalArgumentException("skillId must not be null or blank");
        }
        if (skillName == null || skillName.isBlank()) {
            throw new IllegalArgumentException("skillName must not be null or blank");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("category must not be null or blank");
        }
        
        this.skillId = skillId;
        this.skillName = skillName;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getSkillId() { return skillId; }
    public String getSkillName() { return skillName; }
    public String getCategory() { return category; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
