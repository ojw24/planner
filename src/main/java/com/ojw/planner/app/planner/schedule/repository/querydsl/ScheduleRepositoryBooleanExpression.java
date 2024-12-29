package com.ojw.planner.app.planner.schedule.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDate;

import static com.ojw.planner.app.planner.schedule.domain.QSchedule.schedule;

public class ScheduleRepositoryBooleanExpression {

    protected BooleanExpression checkDate(LocalDate localDate) {
        return schedule.startDtm.goe(localDate.withDayOfMonth(1).atStartOfDay())
                .and(schedule.startDtm.lt(localDate.withDayOfMonth(localDate.lengthOfMonth()).plusDays(1).atStartOfDay()));
    }

}
