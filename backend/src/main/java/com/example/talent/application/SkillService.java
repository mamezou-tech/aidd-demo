package com.example.talent.application;

import com.example.talent.domain.model.Skill;
import com.example.talent.infrastructure.repository.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> findAll() {
        return skillRepository.findAll().stream()
                .map(entity -> entity.toDomain())
                .collect(Collectors.toList());
    }

    public List<Skill> findByCategory(String category) {
        return skillRepository.findByCategory(category).stream()
                .map(entity -> entity.toDomain())
                .collect(Collectors.toList());
    }
}
