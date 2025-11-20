package com.example.talent.infrastructure.repository;

import com.example.talent.domain.model.Organization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class OrganizationRepositoryTest {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Test
    void shouldFindAllOrganizations() {
        List<Organization> organizations = organizationRepository.findAll().stream()
                .map(entity -> entity.toDomain())
                .collect(Collectors.toList());
        
        assertThat(organizations).hasSize(10);
    }

    @Test
    void shouldFindOrganizationById() {
        Optional<Organization> org = organizationRepository.findById("ORG001")
                .map(entity -> entity.toDomain());
        
        assertThat(org).isPresent();
        assertThat(org.get().getOrganizationName()).isEqualTo("経営企画部");
    }
}
