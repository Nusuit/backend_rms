package org.example.rms.repo;

import org.example.rms.entity.InterviewStage;
import org.example.rms.entity.StageOfJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StageOfJobRepository extends JpaRepository<StageOfJob, Integer> {
    void deleteByJobId(Long jobId);

    Optional<StageOfJob> findByJobIdAndStageOrder(Long jobId, Integer stageOrder);

    Optional<StageOfJob> findByJobIdAndStageId(Long jobId, Long stageId);

    Optional<StageOfJob> findByStageIdAndJobId(Long stageId, Long jobId);
}
