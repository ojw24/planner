package com.ojw.planner.app.planner.goal.repository;

import com.ojw.planner.app.planner.goal.domain.Goal;
import com.ojw.planner.app.planner.goal.repository.querydsl.GoalRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long>, GoalRepositoryCustom {

    Optional<Goal> findByGoalIdAndUserUserIdAndIsDeletedIsFalse(Long goalId, String userId);

}
