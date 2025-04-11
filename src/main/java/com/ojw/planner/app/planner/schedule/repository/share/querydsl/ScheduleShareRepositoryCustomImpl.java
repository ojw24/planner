package com.ojw.planner.app.planner.schedule.repository.share.querydsl;

import com.ojw.planner.app.planner.schedule.domain.Schedule;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleFindDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ojw.planner.app.planner.schedule.domain.share.QScheduleShare.scheduleShare;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class ScheduleShareRepositoryCustomImpl extends ScheduleShareRepositoryBooleanExpression implements ScheduleShareRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Schedule> findAll(ScheduleFindDto findDto, String userId) {
        return queryFactory
                .select(scheduleShare.schedule)
                .from(scheduleShare)
                .where(
                        scheduleShare.schedule.goal.isNull()
                        , checkDate(findDto.getSearchDate())
                        , scheduleShare.user.userId.eq(userId)
                        , scheduleShare.schedule.isDeleted.isFalse()
                )
                .fetch();
    }

}
