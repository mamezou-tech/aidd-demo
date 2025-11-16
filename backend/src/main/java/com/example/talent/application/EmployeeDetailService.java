package com.example.talent.application;

import com.example.talent.domain.Employee;
import com.example.talent.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDetailService {

    private final EmployeeRepository employeeRepository;

    public EmployeeDetailService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .filter(employee -> employee.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("社員が見つかりません。ID: " + id));
    }
}
