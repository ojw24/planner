package com.ojw.planner.app.planner.goal.repository.querydsl;

import com.ojw.planner.app.planner.goal.domain.Goal;
import com.ojw.planner.app.planner.goal.domain.dto.GoalFindDto;
import com.ojw.planner.core.domain.DatePeriod;
import com.ojw.planner.core.enumeration.planner.goal.GoalType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.ojw.planner.app.planner.goal.domain.QGoal.goal;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class GoalRepositoryCustomImpl extends GoalRepositoryBooleanExpression implements GoalRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long checkDuplicate(GoalType goalType, DatePeriod period, String userId, Long goalId) {
        return queryFactory
                .select(goal.count())
                .from(goal)
                .where(
                        goal.goalType.eq(goalType)
                        , goalType.checkPeriod(period)
                        , notEqualsGoalId(goalId)
                        , goal.user.userId.eq(userId)
                        , goal.isDeleted.isFalse()
                ).fetchOne();
    }

    @Override
    public Goal get(GoalFindDto findDto, String userId) {
        return queryFactory
                .selectFrom(goal)
                .where(
                        goal.goalType.eq(findDto.getGoalType())
                        , findDto.getGoalType().checkPeriod(new DatePeriod(findDto.getLocalDate(), findDto.getLocalDate()))
                        , goal.user.userId.eq(userId)
                        , goal.isDeleted.isFalse()
                ).fetchOne();
    }

}
