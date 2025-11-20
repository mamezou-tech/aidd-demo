package com.example.talent.application;

import com.example.talent.domain.model.Organization;
import com.example.talent.infrastructure.entity.OrganizationEntity;
import com.example.talent.infrastructure.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private OrganizationService organizationService;

    @Test
    void shouldFindAllOrganizations() {
        OrganizationEntity entity = createOrganizationEntity("ORG001", "経営企画部");
        when(organizationRepository.findAll()).thenReturn(List.of(entity));

        List<Organization> organizations = organizationService.findAll();

        assertThat(organizations).hasSize(1);
        assertThat(organizations.get(0).getOrganizationName()).isEqualTo("経営企画部");
    }

    @Test
    void shouldFindOrganizationById() {
        OrganizationEntity entity = createOrganizationEntity("ORG001", "経営企画部");
        when(organizationRepository.findById("ORG001")).thenReturn(Optional.of(entity));

        Optional<Organization> organization = organizationService.findById("ORG001");

        assertThat(organization).isPresent();
        assertThat(organization.get().getOrganizationId()).isEqualTo("ORG001");
    }

    private OrganizationEntity createOrganizationEntity(String id, String name) {
        return OrganizationEntity.fromDomain(new Organization(
                id, name, LocalDateTime.now(), LocalDateTime.now()
        ));
    }
}
