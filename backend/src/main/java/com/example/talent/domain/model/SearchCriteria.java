package com.example.talent.domain.model;

import java.util.List;

public final class SearchCriteria {
    private final String name;
    private final String organizationId;
    private final String position;
    private final String employmentType;
    private final List<String> skillIds;

    public SearchCriteria(String name, String organizationId, String position, 
                         String employmentType, List<String> skillIds) {
        this.name = name;
        this.organizationId = organizationId;
        this.position = position;
        this.employmentType = employmentType;
        this.skillIds = skillIds;
    }

    public String getName() { return name; }
    public String getOrganizationId() { return organizationId; }
    public String getPosition() { return position; }
    public String getEmploymentType() { return employmentType; }
    public List<String> getSkillIds() { return skillIds; }
}
