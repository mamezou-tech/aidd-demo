package com.example.talent.application;

import com.example.talent.domain.model.Employee;
import com.example.talent.infrastructure.repository.EmployeeRepository;
import com.example.talent.infrastructure.repository.EmployeeSkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EmployeeDetailService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeSkillRepository employeeSkillRepository;

    public EmployeeDetailService(EmployeeRepository employeeRepository, 
                                EmployeeSkillRepository employeeSkillRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeSkillRepository = employeeSkillRepository;
    }

    public EmployeeDetailDTO getDetail(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .map(entity -> entity.toDomain())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + employeeId));

        if (employee.isDeleted()) {
            throw new IllegalArgumentException("Employee is deleted: " + employeeId);
        }

        var skills = employeeSkillRepository.findByEmployeeId(employeeId).stream()
                .map(es -> new EmployeeDetailDTO.SkillDTO(
                    es.getSkill().getSkillName(),
                    es.getLevel()
                ))
                .collect(Collectors.toList());

        return new EmployeeDetailDTO(
            employee.getEmployeeId(),
            employee.getName(),
            employee.getOrganizationId(),
            employee.getPosition(),
            employee.getEmploymentType(),
            employee.getHireDate(),
            employee.getEmail(),
            employee.getPhotoPath(),
            skills
        );
    }
}
