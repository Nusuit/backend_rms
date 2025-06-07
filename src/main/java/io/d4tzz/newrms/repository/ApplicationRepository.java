package io.d4tzz.newrms.repository;

import io.d4tzz.newrms.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>, JpaSpecificationExecutor<Application> {
    Long countByJobId(Long jobId);

    List<Application> findByJobId(Long jobId);

    Optional<Application> findByJobIdAndCandidateId(Long jobId, Long candidateId);

    Page<Application> findByCandidateId(Long candidateId, Pageable pageable);
}
