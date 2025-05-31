package io.d4tzz.newrms.repository;

import io.d4tzz.newrms.entity.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    Page<Skill> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
