package com.example.talent.controller;

import com.example.talent.application.EmployeeSearchService;
import com.example.talent.domain.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeSearchService employeeSearchService;

    public EmployeeController(EmployeeSearchService employeeSearchService) {
        this.employeeSearchService = employeeSearchService;
    }

    @GetMapping
    public Page<Employee> search(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String fullNameKana,
            @RequestParam(required = false) String employeeCode,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String employmentType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("fullName"));
        return employeeSearchService.search(fullName, fullNameKana, employeeCode, 
                email, position, employmentType, pageable);
    }
}
