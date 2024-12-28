package com.ojw.planner.app.planner.goal.repository.querydsl;

import com.ojw.planner.app.planner.goal.domain.Goal;
import com.ojw.planner.app.planner.goal.domain.dto.GoalFindDto;
import com.ojw.planner.core.domain.DatePeriod;
import com.ojw.planner.core.enumeration.planner.goal.GoalType;

public interface GoalRepositoryCustom {

    Long checkDuplicate(GoalType goalType, DatePeriod period, String userId, Long goalId);

    Goal get(GoalFindDto findDto, String userId);

}
