package io.d4tzz.newrms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import io.d4tzz.newrms.entity.RecruitmentProcess;

public interface RecruitmentProcessRepository extends JpaRepository<RecruitmentProcess, Long> {
    Page<RecruitmentProcess> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

//    Page<RecruitmentProcess> findAll
}
