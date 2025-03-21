package org.example.rms.repo;

import org.example.rms.entity.Role;
import org.example.rms.utils.RmsRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);

    default Role getCandidateRole() {
        return findByRoleName(RmsRole.CANDIDATE).orElse(null);
    }

    default Role getRecruiterRole() {
        return findByRoleName(RmsRole.RECRUITER).orElse(null);
    }
}
