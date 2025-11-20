package com.example.talent.infrastructure.repository;

import com.example.talent.domain.model.Skill;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class SkillRepositoryTest {

    @Autowired
    private SkillRepository skillRepository;

    @Test
    void shouldFindAllSkills() {
        List<Skill> skills = skillRepository.findAll().stream()
                .map(entity -> entity.toDomain())
                .collect(Collectors.toList());
        
        assertThat(skills).hasSize(20);
    }

    @Test
    void shouldFindSkillById() {
        Optional<Skill> skill = skillRepository.findById("SK001")
                .map(entity -> entity.toDomain());
        
        assertThat(skill).isPresent();
        assertThat(skill.get().getSkillName()).isEqualTo("Java");
    }

    @Test
    void shouldFindSkillsByCategory() {
        List<Skill> skills = skillRepository.findByCategory("プログラミング言語").stream()
                .map(entity -> entity.toDomain())
                .collect(Collectors.toList());
        
        assertThat(skills).isNotEmpty();
        assertThat(skills).allMatch(s -> s.getCategory().equals("プログラミング言語"));
    }
}
