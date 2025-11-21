package com.example.talent.application;

import com.example.talent.domain.model.Employee;
import com.example.talent.domain.model.SearchCriteria;
import com.example.talent.infrastructure.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EmployeeSearchService {

    private final EmployeeRepository employeeRepository;

    public EmployeeSearchService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findByDeletedFalse().stream()
                .map(entity -> entity.toDomain())
                .collect(Collectors.toList());
    }

    public Optional<Employee> findById(String employeeId) {
        return employeeRepository.findById(employeeId)
                .map(entity -> entity.toDomain());
    }

    public List<Employee> findByOrganization(String organizationId) {
        return employeeRepository.findByOrganizationIdAndDeletedFalse(organizationId).stream()
                .map(entity -> entity.toDomain())
                .collect(Collectors.toList());
    }

    public List<Employee> searchByName(String name) {
        return employeeRepository.findByNameContainingAndDeletedFalse(name).stream()
                .map(entity -> entity.toDomain())
                .collect(Collectors.toList());
    }

    public List<Employee> search(SearchCriteria criteria) {
        // Skill search with AND condition (highest priority)
        if (criteria.getSkillIds() != null && !criteria.getSkillIds().isEmpty()) {
            return employeeRepository.findBySkillsAndDeletedFalse(
                criteria.getSkillIds(), 
                (long) criteria.getSkillIds().size()
            ).stream()
                .map(entity -> entity.toDomain())
                .collect(Collectors.toList());
        }
        
        if (criteria.getOrganizationId() != null) {
            return findByOrganization(criteria.getOrganizationId());
        }
        if (criteria.getName() != null) {
            return searchByName(criteria.getName());
        }
        return findAll();
    }
}
