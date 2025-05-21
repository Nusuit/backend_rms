package io.d4tzz.newrms.repository;

import io.d4tzz.newrms.entity.Schedule;
import io.d4tzz.newrms.entity.Stage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Page<Schedule> findByJobIdAndJobStageId(Long jobId, Long jobStageId, Pageable pageable);

    Optional<Schedule> findByJobIdAndJobStageIdAndId(Long jobId, Long jobStageId, Long scheduleId);

    List<Schedule> findByJobIdAndJobStageOrder(Long jobId, Integer order);
}
