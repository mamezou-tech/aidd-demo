package com.example.talent.application;

import java.time.LocalDate;
import java.util.List;

public class EmployeeDetailDTO {
    private String employeeId;
    private String name;
    private String organizationId;
    private String position;
    private String employmentType;
    private LocalDate hireDate;
    private String email;
    private String photoPath;
    private List<SkillDTO> skills;
    
    public EmployeeDetailDTO(String employeeId, String name, String organizationId, String position,
                            String employmentType, LocalDate hireDate, String email, String photoPath,
                            List<SkillDTO> skills) {
        this.employeeId = employeeId;
        this.name = name;
        this.organizationId = organizationId;
        this.position = position;
        this.employmentType = employmentType;
        this.hireDate = hireDate;
        this.email = email;
        this.photoPath = photoPath;
        this.skills = skills;
    }
    
    public String getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public String getOrganizationId() { return organizationId; }
    public String getPosition() { return position; }
    public String getEmploymentType() { return employmentType; }
    public LocalDate getHireDate() { return hireDate; }
    public String getEmail() { return email; }
    public String getPhotoPath() { return photoPath; }
    public List<SkillDTO> getSkills() { return skills; }
    
    public static class SkillDTO {
        private String skillName;
        private int level;
        
        public SkillDTO(String skillName, int level) {
            this.skillName = skillName;
            this.level = level;
        }
        
        public String getSkillName() { return skillName; }
        public int getLevel() { return level; }
    }
}
