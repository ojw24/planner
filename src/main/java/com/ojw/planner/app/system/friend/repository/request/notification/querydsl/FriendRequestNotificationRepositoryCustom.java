package com.ojw.planner.app.system.friend.repository.request.notification.querydsl;

import com.ojw.planner.app.system.friend.domain.request.notification.FriendRequestNotification;

import java.util.List;

public interface FriendRequestNotificationRepositoryCustom {

    List<FriendRequestNotification> findAll(String userId);

}
