package com.example.talent.performance;

import com.example.talent.infrastructure.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class EmployeeSearchPerformanceTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void searchShouldCompleteWithin5Seconds() {
        long start = System.currentTimeMillis();
        employeeRepository.findByNameContainingAndDeletedFalse("田中");
        long duration = System.currentTimeMillis() - start;
        
        assertTrue(duration < 5000, "Search took " + duration + "ms");
    }

    @Test
    void listAllShouldCompleteWithin5Seconds() {
        long start = System.currentTimeMillis();
        employeeRepository.findByDeletedFalse();
        long duration = System.currentTimeMillis() - start;
        
        assertTrue(duration < 5000, "List took " + duration + "ms");
    }

    @Test
    void detailShouldCompleteWithin5Seconds() {
        long start = System.currentTimeMillis();
        employeeRepository.findById("E001");
        long duration = System.currentTimeMillis() - start;
        
        assertTrue(duration < 5000, "Detail took " + duration + "ms");
    }
}
