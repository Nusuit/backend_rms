package io.d4tzz.newrms.repository;

import io.d4tzz.newrms.entity.Interview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    Page<Interview> findByStageIdAndApplicationJobIdAndScheduleIsNull(Long stageId, Long applicationJobId, Pageable pageable);

    Page<Interview> findByScheduleId(Long scheduleId, Pageable pageable);

    Optional<Interview> findByApplicationIdAndStageId(Long applicationId, Long stageId);

    Page<Interview> findByApplicationJobRecruiterId(Long recruiterId, Pageable pageable);
}
