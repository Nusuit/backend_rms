package io.d4tzz.newrms.repository;

import io.d4tzz.newrms.entity.CandidateAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateAuthRepository extends JpaRepository<CandidateAuth, Long> {
    Optional<CandidateAuth> findByEmail(String email);
}
