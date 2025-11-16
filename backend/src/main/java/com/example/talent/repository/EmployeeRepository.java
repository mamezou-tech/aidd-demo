package com.example.talent.repository;

import com.example.talent.domain.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    
    Page<Employee> findByFullNameContaining(String fullName, Pageable pageable);
    
    Page<Employee> findByFullNameKanaContaining(String fullNameKana, Pageable pageable);
    
    Page<Employee> findByDeletedAtIsNull(Pageable pageable);
}
