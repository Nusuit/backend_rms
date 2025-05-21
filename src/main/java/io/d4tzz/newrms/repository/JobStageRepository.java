package io.d4tzz.newrms.repository;

import io.d4tzz.newrms.entity.JobStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobStageRepository extends JpaRepository<JobStage, Long> {
    Optional<JobStage> findByJobIdAndId(Long jobId, Long jobStageId);

    Optional<JobStage> findByJobIdAndOrder(Long jobId, Integer order);
}
