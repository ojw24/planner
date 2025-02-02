package com.ojw.planner.app.system.user.repository;

import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.app.system.user.repository.querydsl.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {

    Optional<User> findByUserIdAndIsDeletedIsFalse(String userId);

    Optional<User> findByEmailAndIsDeletedIsFalse(String email);

    Optional<User> findByEmailAndUserIdNotAndIsDeletedIsFalse(String email, String userId);

}
