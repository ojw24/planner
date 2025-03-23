package com.ojw.planner.app.planner.schedule.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDate;

import static com.ojw.planner.app.planner.schedule.domain.QSchedule.schedule;

public class ScheduleRepositoryBooleanExpression {

    protected BooleanExpression checkDate(LocalDate searchDate) {

        LocalDate base = searchDate.withDayOfMonth(1);
        base = base.minusDays(base.getDayOfWeek().getValue());

        return schedule.startDtm.goe(base.atStartOfDay())
                .and(schedule.startDtm.lt(base.plusDays(42).atStartOfDay()));

    }

}
