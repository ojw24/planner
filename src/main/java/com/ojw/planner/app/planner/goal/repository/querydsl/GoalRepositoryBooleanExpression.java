package com.ojw.planner.app.planner.goal.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.util.ObjectUtils;

import static com.ojw.planner.app.planner.goal.domain.QGoal.goal;

public class GoalRepositoryBooleanExpression {

    protected BooleanExpression notEqualsGoalId(Long goalId) {
        return ObjectUtils.isEmpty(goalId)
                ? null
                : goal.goalId.ne(goalId);
    }

}
