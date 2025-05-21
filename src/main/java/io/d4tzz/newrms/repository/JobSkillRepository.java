package io.d4tzz.newrms.repository;

import io.d4tzz.newrms.entity.JobSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobSkillRepository extends JpaRepository<JobSkill, Long> {
    List<JobSkill> findByJobId(Long jobId);

    Optional<JobSkill> findByJobIdAndSkillId(Long jobId, Long skillId);

    void deleteByJobId(Long jobId);
}
