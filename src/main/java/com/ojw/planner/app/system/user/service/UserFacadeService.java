package com.ojw.planner.app.system.user.service;

import com.ojw.planner.app.community.board.service.comment.notification.BoardCommentNotificationService;
import com.ojw.planner.app.planner.schedule.service.request.notification.ScheduleShareRequestNotificationService;
import com.ojw.planner.app.system.friend.service.request.notification.FriendRequestNotificationService;
import com.ojw.planner.app.system.user.domain.dto.redis.PwdResetRequest;
import com.ojw.planner.app.system.user.domain.redis.PwdResetKey;
import com.ojw.planner.app.system.user.domain.security.CustomUserDetails;
import com.ojw.planner.app.system.user.service.redis.PwdResetKeyService;
import com.ojw.planner.core.domain.Notification;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserFacadeService {

    @Value("${spring.data.redis.expire.password}")
    private Long passwordExpire;

    private final UserService userService;

    private final FriendRequestNotificationService friendRequestNotificationService;

    private final ScheduleShareRequestNotificationService shareRequestNotificationService;

    private final BoardCommentNotificationService notificationService;

    private final PwdResetKeyService pwdResetKeyService;

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

    /**
     * 비밀번호 재설정 메일 전송
     *
     * @param userId - 사용자 아이디
     */
    public void sendPasswordReset(String userId) {

        String key = pwdResetKeyService.saveKey(
                PwdResetKey.builder()
                        .userId(userId)
                        .key(UUID.randomUUID().toString())
                        .expire(passwordExpire)
                        .build()
        );

        userService.sendPasswordReset(userId, key);

    }

    /**
     * 비밀번호 재설정
     *
     * @param request - 재설정 요청
     */
    @Transactional
    public void userPasswordReset(PwdResetRequest request) {

        PwdResetKey key = pwdResetKeyService.getKey(request.getKey());
        if(key == null) throw new ResponseException("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        if(!key.getUserId().equalsIgnoreCase(request.getUserId()))
            throw new ResponseException("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);

        userService.userPasswordReset(request);

    }

}
