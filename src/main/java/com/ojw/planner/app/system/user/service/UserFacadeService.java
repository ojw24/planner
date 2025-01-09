package com.ojw.planner.app.system.user.service;

import com.ojw.planner.app.community.board.service.comment.notification.BoardCommentNotificationService;
import com.ojw.planner.app.planner.schedule.service.request.notification.ScheduleShareRequestNotificationService;
import com.ojw.planner.app.system.friend.service.request.notification.FriendRequestNotificationService;
import com.ojw.planner.app.system.user.domain.security.CustomUserDetails;
import com.ojw.planner.core.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserFacadeService {

    private final FriendRequestNotificationService friendRequestNotificationService;

    private final ScheduleShareRequestNotificationService shareRequestNotificationService;

    private final BoardCommentNotificationService notificationService;

    /**
    * 사용자 알림 목록 조회
    */
    public List<Notification> findNotifications() {

        String userId = CustomUserDetails.getDetails().getUserId();

        List<Notification> notifications = new ArrayList<>();
        notifications.addAll(friendRequestNotificationService.findNotifications(userId));
        notifications.addAll(shareRequestNotificationService.findNotifications(userId));
        notifications.addAll(notificationService.findNotifications(userId));

        Collections.sort(notifications, (n1, n2) -> n2.getRegDtm().compareTo(n1.getRegDtm()));
        return notifications;

    }

}
