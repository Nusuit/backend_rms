package org.example.rms.repo;

import org.example.rms.entity.SkillOfJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillOfJobRepository extends JpaRepository<SkillOfJob, Long> {
    Optional<SkillOfJob> findByJobIdAndSkillId(Long jobId, Long skillId);

    void deleteByJobId(Long jobId);
}
