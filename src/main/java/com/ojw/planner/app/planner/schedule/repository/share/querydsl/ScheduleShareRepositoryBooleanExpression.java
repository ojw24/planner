package com.ojw.planner.app.planner.schedule.repository.share.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDate;

import static com.ojw.planner.app.planner.schedule.domain.QSchedule.schedule;
import static com.ojw.planner.app.planner.schedule.domain.share.QScheduleShare.scheduleShare;

public class ScheduleShareRepositoryBooleanExpression {

    protected BooleanExpression checkDate(LocalDate searchDate) {

        LocalDate base = searchDate.withDayOfMonth(1);
        base = base.minusDays(base.getDayOfWeek().getValue());

        return scheduleShare.schedule.startDtm.goe(base.atStartOfDay())
                .and(scheduleShare.schedule.startDtm.lt(base.plusDays(42).atStartOfDay()))
                .or(
                        schedule.endDtm.goe(base.atStartOfDay())
                                .and(schedule.endDtm.lt(base.plusDays(42).atStartOfDay()))
                );

    }

}
