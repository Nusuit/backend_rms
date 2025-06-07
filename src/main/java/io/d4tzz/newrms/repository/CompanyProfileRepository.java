package io.d4tzz.newrms.repository;

import io.d4tzz.newrms.entity.CompanyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {
    // Custom query methods can be added here if needed
} 