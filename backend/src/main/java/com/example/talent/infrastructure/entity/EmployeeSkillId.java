package com.example.talent.infrastructure.entity;

import java.io.Serializable;
import java.util.Objects;

public class EmployeeSkillId implements Serializable {
    private String employeeId;
    private String skillId;
    
    public EmployeeSkillId() {}
    
    public EmployeeSkillId(String employeeId, String skillId) {
        this.employeeId = employeeId;
        this.skillId = skillId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeSkillId that = (EmployeeSkillId) o;
        return Objects.equals(employeeId, that.employeeId) && Objects.equals(skillId, that.skillId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(employeeId, skillId);
    }
}
