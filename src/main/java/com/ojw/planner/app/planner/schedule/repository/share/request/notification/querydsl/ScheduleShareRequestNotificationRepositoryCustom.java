package com.ojw.planner.app.planner.schedule.repository.share.request.notification.querydsl;

import com.ojw.planner.app.planner.schedule.domain.share.request.notification.ScheduleShareRequestNotification;

import java.util.List;

public interface ScheduleShareRequestNotificationRepositoryCustom {

    List<ScheduleShareRequestNotification> findAll(String userId);

}
