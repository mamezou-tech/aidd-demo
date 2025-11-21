package com.example.talent.infrastructure.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employee_skills")
@IdClass(EmployeeSkillId.class)
public class EmployeeSkillEntity {
    
    @Id
    @Column(name = "employee_id")
    private String employeeId;
    
    @Id
    @Column(name = "skill_id")
    private String skillId;
    
    @Column(name = "level")
    private int level;
    
    @ManyToOne
    @JoinColumn(name = "skill_id", insertable = false, updatable = false)
    private SkillEntity skill;
    
    protected EmployeeSkillEntity() {}
    
    public String getEmployeeId() { return employeeId; }
    public String getSkillId() { return skillId; }
    public int getLevel() { return level; }
    public SkillEntity getSkill() { return skill; }
}
