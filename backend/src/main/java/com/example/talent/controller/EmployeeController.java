package com.example.talent.controller;

import com.example.talent.application.EmployeeDetailService;
import com.example.talent.application.EmployeeSearchService;
import com.example.talent.domain.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeSearchService employeeSearchService;
    private final EmployeeDetailService employeeDetailService;

    public EmployeeController(EmployeeSearchService employeeSearchService,
                              EmployeeDetailService employeeDetailService) {
        this.employeeSearchService = employeeSearchService;
        this.employeeDetailService = employeeDetailService;
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

    @GetMapping("/{id}")
    public Employee getById(@PathVariable Long id) {
        return employeeDetailService.findById(id);
    }
}
