package org.example.rms.repo;

import org.example.rms.entity.Job;
import org.example.rms.entity.JobStatus;
import org.example.rms.entity.Recruiter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByRecruiter(Recruiter recruiter);

    List<Job> findByRecruiterId(Long recruiterId);

    Page<Job> findByRecruiterId(Long recruiterId, Pageable pageable);

    Optional<Job> findByIdAndRecruiterId(Long jobId, Long recruiterId);

    Page<Job> findByStatus(JobStatus status, Pageable pageable);

}
