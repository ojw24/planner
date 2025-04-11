package com.ojw.planner.app.system.friend.service.request.notification;

import com.ojw.planner.app.system.friend.domain.dto.request.notification.FriendRequestNotificationDto;
import com.ojw.planner.app.system.friend.domain.request.notification.FriendRequestNotification;
import com.ojw.planner.app.system.friend.repository.request.notification.FriendRequestNotificationRepository;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FriendRequestNotificationService {

    private final FriendRequestNotificationRepository notiRepository;

    @Transactional
    public FriendRequestNotification createNotification(FriendRequestNotification notification) {
        return notiRepository.save(notification);
    }

    public List<FriendRequestNotificationDto> findNotifications(String userId) {
        return notiRepository.findAll(userId).stream().map(FriendRequestNotificationDto::of).collect(Collectors.toList());
    }

    public FriendRequestNotification getNotification(Long notiId) {
        return notiRepository.findById(notiId)
                .orElseThrow(() -> new ResponseException(
                        "not exist friend request notification : " + notiId
                        , HttpStatus.NOT_FOUND
                ));
    }

    @Transactional
    public void deleteNotification(Long notiId) {
        notiRepository.deleteById(notiId);
    }

}
