package io.d4tzz.newrms.repository;

import io.d4tzz.newrms.entity.SavedJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
    Page<SavedJob> findByCandidateId(Long candidateId, Pageable pageable);
    Optional<SavedJob> findByJobIdAndCandidateId(Long jobId, Long candidateId);
    void deleteByJobIdAndCandidateId(Long jobId, Long candidateId);
}
