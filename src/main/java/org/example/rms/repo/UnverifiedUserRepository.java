package org.example.rms.repo;

import org.example.rms.entity.UnverifiedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnverifiedUserRepository extends JpaRepository<UnverifiedUser, Long> {
    Optional<UnverifiedUser> findByEmail(String email);
}
