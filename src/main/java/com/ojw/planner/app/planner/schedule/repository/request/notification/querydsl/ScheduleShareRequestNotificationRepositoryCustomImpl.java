package com.ojw.planner.app.planner.schedule.repository.request.notification.querydsl;

import com.ojw.planner.app.planner.schedule.domain.request.notification.ScheduleShareRequestNotification;
import com.ojw.planner.core.enumeration.common.NotificationType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ojw.planner.app.planner.schedule.domain.request.QScheduleShareRequest.scheduleShareRequest;
import static com.ojw.planner.app.planner.schedule.domain.request.notification.QScheduleShareRequestNotification.scheduleShareRequestNotification;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleShareRequestNotificationRepositoryCustomImpl implements ScheduleShareRequestNotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ScheduleShareRequestNotification> findAll(String userId) {
        return queryFactory
                .selectFrom(scheduleShareRequestNotification)
                .join(scheduleShareRequest)
                .on(scheduleShareRequestNotification.request.eq(scheduleShareRequest))
                .where(
                        (
                                scheduleShareRequestNotification.notiType.eq(NotificationType.REQUEST)
                                        .and(scheduleShareRequest.target.userId.equalsIgnoreCase(userId))
                        )
                                .or(
                                        scheduleShareRequestNotification.notiType.ne(NotificationType.REQUEST)
                                                .and(scheduleShareRequest.requester.userId.equalsIgnoreCase(userId))
                                )
                )
                .orderBy(scheduleShareRequestNotification.regDtm.desc())
                .fetch();
    }

}
