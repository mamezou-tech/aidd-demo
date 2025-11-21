package com.example.talent.infrastructure.entity;

import com.example.talent.domain.model.Organization;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "organizations")
public class OrganizationEntity {
    
    @Id
    @Column(name = "organization_id", length = 36)
    private String organizationId;
    
    @Column(name = "organization_name", nullable = false, length = 100)
    private String organizationName;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected OrganizationEntity() {}

    public Organization toDomain() {
        return new Organization(organizationId, organizationName, createdAt, updatedAt);
    }

    public static OrganizationEntity fromDomain(Organization org) {
        OrganizationEntity entity = new OrganizationEntity();
        entity.organizationId = org.getOrganizationId();
        entity.organizationName = org.getOrganizationName();
        entity.createdAt = org.getCreatedAt();
        entity.updatedAt = org.getUpdatedAt();
        return entity;
    }
}
