package org.example.rms.repo;

import org.example.rms.entity.Application;
import org.example.rms.entity.ApplicationStatus;
import org.example.rms.entity.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByJobId(Long jobId);

    Page<Application> findByJobIdAndJobRecruiterId(Long jobId, Long recruiterId, Pageable pageable);

    Optional<Application> findByJobIdAndCandidateId(Long jobId, Long recruiterId);

    Page<Application> findByCandidateId(Long candidateId, Pageable pageable);

    List<Application> findByCandidateIdAndStatusIsNot(Long candidateId, ApplicationStatus status);

    Optional<Application> findByJobIdAndCandidateIdAndStatus(Long jobId, Long candidateId, ApplicationStatus applicationStatus);

    Optional<Application> findByJobIdAndCandidateIdAndStatusIsNot(Long jobId, Long candidateId, ApplicationStatus applicationStatus);

    Optional<Application> findByIdAndJobRecruiterId(Long applicationId, Long recruiterId);

    List<Application> findByCandidateId(Long candidateId);

    Optional<Application> findByIdAndCandidateId(Long applicationId, Long candidateId);
}
