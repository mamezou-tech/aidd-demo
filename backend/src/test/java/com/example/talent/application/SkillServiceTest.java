package com.example.talent.application;

import com.example.talent.domain.model.Skill;
import com.example.talent.infrastructure.entity.SkillEntity;
import com.example.talent.infrastructure.repository.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillService skillService;

    @Test
    void shouldFindAllSkills() {
        SkillEntity entity = createSkillEntity("SK001", "Java", "プログラミング言語");
        when(skillRepository.findAll()).thenReturn(List.of(entity));

        List<Skill> skills = skillService.findAll();

        assertThat(skills).hasSize(1);
        assertThat(skills.get(0).getSkillName()).isEqualTo("Java");
    }

    @Test
    void shouldFindSkillsByCategory() {
        SkillEntity entity = createSkillEntity("SK001", "Java", "プログラミング言語");
        when(skillRepository.findByCategory("プログラミング言語")).thenReturn(List.of(entity));

        List<Skill> skills = skillService.findByCategory("プログラミング言語");

        assertThat(skills).hasSize(1);
        assertThat(skills.get(0).getCategory()).isEqualTo("プログラミング言語");
    }

    private SkillEntity createSkillEntity(String id, String name, String category) {
        return SkillEntity.fromDomain(new Skill(
                id, name, category, LocalDateTime.now(), LocalDateTime.now()
        ));
    }
}
