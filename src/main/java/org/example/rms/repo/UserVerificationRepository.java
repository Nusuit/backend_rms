package org.example.rms.repo;

import org.example.rms.entity.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {
}
