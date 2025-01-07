package com.ojw.planner.app.planner.schedule.repository.request.notification.querydsl;

import com.ojw.planner.app.planner.schedule.domain.request.notification.ScheduleShareRequestNotification;

import java.util.List;

public interface ScheduleShareRequestNotificationRepositoryCustom {

    List<ScheduleShareRequestNotification> findAll(String userId);

}
