package com.example.talent.infrastructure.repository;

import com.example.talent.infrastructure.entity.EmployeeSkillEntity;
import com.example.talent.infrastructure.entity.EmployeeSkillId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkillEntity, EmployeeSkillId> {
    List<EmployeeSkillEntity> findByEmployeeId(String employeeId);
}
