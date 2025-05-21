package io.d4tzz.newrms.repository;

import io.d4tzz.newrms.entity.Role;
import io.d4tzz.newrms.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName name);
}
