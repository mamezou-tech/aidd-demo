package com.example.talent.infrastructure.entity;

import com.example.talent.domain.model.Skill;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "skills")
public class SkillEntity {
    
    @Id
    @Column(name = "skill_id", length = 36)
    private String skillId;
    
    @Column(name = "skill_name", nullable = false, length = 100)
    private String skillName;
    
    @Column(name = "category", nullable = false, length = 50)
    private String category;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected SkillEntity() {}

    public String getSkillId() { return skillId; }
    public String getSkillName() { return skillName; }
    public String getCategory() { return category; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public Skill toDomain() {
        return new Skill(skillId, skillName, category, createdAt, updatedAt);
    }

    public static SkillEntity fromDomain(Skill skill) {
        SkillEntity entity = new SkillEntity();
        entity.skillId = skill.getSkillId();
        entity.skillName = skill.getSkillName();
        entity.category = skill.getCategory();
        entity.createdAt = skill.getCreatedAt();
        entity.updatedAt = skill.getUpdatedAt();
        return entity;
    }
}
