package com.example.talent.application;

import com.example.talent.domain.model.Organization;
import com.example.talent.infrastructure.repository.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public List<Organization> findAll() {
        return organizationRepository.findAll().stream()
                .map(entity -> entity.toDomain())
                .collect(Collectors.toList());
    }

    public Optional<Organization> findById(String organizationId) {
        return organizationRepository.findById(organizationId)
                .map(entity -> entity.toDomain());
    }
}
