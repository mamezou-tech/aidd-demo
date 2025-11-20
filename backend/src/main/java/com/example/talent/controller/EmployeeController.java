package com.example.talent.controller;

import com.example.talent.application.EmployeeDetailDTO;
import com.example.talent.application.EmployeeDetailService;
import com.example.talent.application.EmployeeSearchService;
import com.example.talent.domain.model.Employee;
import com.example.talent.domain.model.SearchCriteria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
    public Map<String, Object> getAllEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String organizationId,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String employmentType,
            @RequestParam(required = false) List<String> skillIds,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        SearchCriteria criteria = new SearchCriteria(name, organizationId, position, employmentType, skillIds);
        List<Employee> employees = employeeSearchService.search(criteria);
        
        int start = page * size;
        int end = Math.min(start + size, employees.size());
        List<Employee> pageContent = employees.subList(start, end);
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", pageContent);
        response.put("totalPages", (int) Math.ceil((double) employees.size() / size));
        response.put("totalElements", employees.size());
        response.put("number", page);
        response.put("size", size);
        
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDetailDTO> getEmployeeById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(employeeDetailService.getDetail(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public List<Employee> searchEmployees(@RequestParam String name) {
        return employeeSearchService.searchByName(name);
    }
}
