package org.example.rms.repo;

import org.example.rms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
