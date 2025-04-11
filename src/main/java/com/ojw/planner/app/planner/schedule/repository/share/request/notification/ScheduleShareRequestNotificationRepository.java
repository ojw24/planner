package com.ojw.planner.app.planner.schedule.repository.share.request.notification;

import com.ojw.planner.app.planner.schedule.domain.share.request.notification.ScheduleShareRequestNotification;
import com.ojw.planner.app.planner.schedule.repository.share.request.notification.querydsl.ScheduleShareRequestNotificationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleShareRequestNotificationRepository extends JpaRepository<ScheduleShareRequestNotification, Long>, ScheduleShareRequestNotificationRepositoryCustom {
}
