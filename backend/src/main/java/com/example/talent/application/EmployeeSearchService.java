package com.example.talent.application;

import com.example.talent.domain.Employee;
import com.example.talent.repository.EmployeeRepository;
import com.example.talent.repository.EmployeeSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class EmployeeSearchService {

    private final EmployeeRepository employeeRepository;

    public EmployeeSearchService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Page<Employee> search(String name, String nameKana, String employeeCode, 
                                  String email, String position, String employmentType,
                                  org.springframework.data.domain.Pageable pageable) {
        Specification<Employee> spec = Specification
                .where(EmployeeSpecification.hasFullName(name))
                .and(EmployeeSpecification.hasFullNameKana(nameKana))
                .and(EmployeeSpecification.hasEmployeeCode(employeeCode))
                .and(EmployeeSpecification.hasEmail(email))
                .and(EmployeeSpecification.hasPosition(position))
                .and(EmployeeSpecification.hasEmploymentType(employmentType))
                .and(EmployeeSpecification.isNotDeleted());

        return employeeRepository.findAll(spec, pageable);
    }
}
