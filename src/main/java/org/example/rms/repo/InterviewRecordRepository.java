package org.example.rms.repo;

import org.example.rms.entity.InterviewRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewRecordRepository extends JpaRepository<InterviewRecord, Long> {
    List<InterviewRecord> findByStageIdAndScheduleIsNull(Long stageId);

    Page<InterviewRecord> findByStageIdAndScheduleIsNull(Long stageId, Pageable pageable);

    Optional<InterviewRecord> findByIdAndScheduleId(Long recordId, Long scheduleId);

    Page<InterviewRecord> findByApplicationJobIdAndStageIdAndScheduleIsNull(Long jobId, Long stageId, Pageable pageable);

    Optional<InterviewRecord> findByIdAndApplicationJobIdAndStageIdAndScheduleIsNull(Long recordId, Long jobId, Long stageId);

    Page<InterviewRecord> findByScheduleId(Long scheduleId, Pageable pageable);
}
