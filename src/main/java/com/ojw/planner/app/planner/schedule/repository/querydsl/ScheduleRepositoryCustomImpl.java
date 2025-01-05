package com.ojw.planner.app.planner.schedule.repository.querydsl;

import com.ojw.planner.app.planner.schedule.domain.Schedule;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleFindDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ojw.planner.app.planner.schedule.domain.QSchedule.schedule;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class ScheduleRepositoryCustomImpl extends ScheduleRepositoryBooleanExpression implements ScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Schedule> findAll(ScheduleFindDto findDto, String userId) {
        return queryFactory
                .selectFrom(schedule)
                .where(
                        schedule.goal.isNull()
                        , checkDate(findDto.getLocalDate())
                        , schedule.user.userId.eq(userId)
                        , schedule.isDeleted.isFalse()
                )
                .fetch();
    }

}
