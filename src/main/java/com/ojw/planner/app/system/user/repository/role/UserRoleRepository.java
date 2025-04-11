package com.ojw.planner.app.system.user.repository.role;

import com.ojw.planner.app.system.user.domain.role.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.UserRolePK> {
}
