package com.example.talent.domain.model;

import java.time.LocalDateTime;

public final class Organization {
    private final String organizationId;
    private final String organizationName;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Organization(String organizationId, String organizationName, 
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (organizationId == null || organizationId.isBlank()) {
            throw new IllegalArgumentException("organizationId must not be null or blank");
        }
        if (organizationName == null || organizationName.isBlank()) {
            throw new IllegalArgumentException("organizationName must not be null or blank");
        }
        
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getOrganizationId() { return organizationId; }
    public String getOrganizationName() { return organizationName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
