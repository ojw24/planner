package com.ojw.planner.app.system.friend.repository.request.notification;

import com.ojw.planner.app.system.friend.domain.request.notification.FriendRequestNotification;
import com.ojw.planner.app.system.friend.repository.request.notification.querydsl.FriendRequestNotificationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestNotificationRepository extends JpaRepository<FriendRequestNotification, Long>, FriendRequestNotificationRepositoryCustom {
}
