package io.d4tzz.newrms.repository;

import io.d4tzz.newrms.entity.RecruiterAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruiterAuthRepository extends JpaRepository<RecruiterAuth, Long> {
    Optional<RecruiterAuth> findByUsername(String username);
}
