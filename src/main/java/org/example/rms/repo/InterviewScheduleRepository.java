package org.example.rms.repo;

import org.example.rms.entity.InterviewSchedule;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Long> {
    Optional<InterviewSchedule> findByIdAndStageId(Long id, Long stageId);

    Page<InterviewSchedule> findByJobIdAndStageId(Long jobId, Long stageId, Pageable pageable);
}
