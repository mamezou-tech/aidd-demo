package com.example.talent.infrastructure.repository;

import com.example.talent.infrastructure.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, String> {
    
    List<EmployeeEntity> findByDeletedFalse();
    
    List<EmployeeEntity> findByOrganizationIdAndDeletedFalse(String organizationId);
    
    List<EmployeeEntity> findByNameContainingAndDeletedFalse(String name);
    
    @Query(value = "SELECT DISTINCT e.* FROM employees e " +
           "JOIN employee_skills es ON e.employee_id = es.employee_id " +
           "WHERE es.skill_id IN :skillIds AND e.deleted = false " +
           "GROUP BY e.employee_id " +
           "HAVING COUNT(DISTINCT es.skill_id) = :skillCount " +
           "ORDER BY e.name", 
           nativeQuery = true)
    List<EmployeeEntity> findBySkillsAndDeletedFalse(@Param("skillIds") List<String> skillIds, @Param("skillCount") long skillCount);
}
