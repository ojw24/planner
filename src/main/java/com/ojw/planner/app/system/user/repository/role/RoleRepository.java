package com.ojw.planner.app.system.user.repository.role;

import com.ojw.planner.app.system.user.domain.role.Role;
import com.ojw.planner.core.enumeration.system.user.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByAuthority(Authority authority);

}
