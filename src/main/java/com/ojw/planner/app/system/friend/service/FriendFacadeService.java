package com.ojw.planner.app.system.friend.service;

import com.ojw.planner.app.system.friend.domain.Friend;
import com.ojw.planner.app.system.friend.domain.dto.FriendDto;
import com.ojw.planner.app.system.friend.domain.dto.FriendUpdateDto;
import com.ojw.planner.app.system.friend.domain.dto.group.FriendGroupCreateDto;
import com.ojw.planner.app.system.friend.domain.dto.group.FriendGroupDto;
import com.ojw.planner.app.system.friend.domain.dto.group.FriendGroupUpdateDto;
import com.ojw.planner.app.system.friend.domain.dto.request.FriendRequestCreateDto;
import com.ojw.planner.app.system.friend.domain.dto.request.FriendRequestDto;
import com.ojw.planner.app.system.friend.domain.dto.request.notification.FriendRequestNotificationDto;
import com.ojw.planner.app.system.friend.domain.group.FriendGroup;
import com.ojw.planner.app.system.friend.domain.request.FriendRequest;
import com.ojw.planner.app.system.friend.domain.request.notification.FriendRequestNotification;
import com.ojw.planner.app.system.friend.service.group.FriendGroupService;
import com.ojw.planner.app.system.friend.service.request.FriendRequestService;
import com.ojw.planner.app.system.friend.service.request.notification.FriendRequestNotificationService;
import com.ojw.planner.app.system.user.domain.security.CustomUserDetails;
import com.ojw.planner.app.system.user.service.UserService;
import com.ojw.planner.config.RabbitMqConfigProperties;
import com.ojw.planner.core.enumeration.inner.FriendRoutes;
import com.ojw.planner.core.enumeration.system.friend.NotificationType;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FriendFacadeService {

    private final FriendGroupService friendGroupService;

    private final FriendRequestService friendRequestService;

    private final FriendRequestNotificationService friendRequestNotificationService;

    private final FriendService friendService;

    private final UserService userService; //TODO : AOP 대체?

    private final RabbitTemplate rabbitTemplate;

    private final RabbitMqConfigProperties rabbitMqProp;

    /**
     * 친구 그룹 등록
     *
     * @param createDto - 등록 정보
     * @return 생성된 친구 그룹 아이디
     */
    @Transactional
    public Long createFriendGroup(FriendGroupCreateDto createDto) {
        return friendGroupService.createFriendGroup(
                createDto
                , userService.getUser(CustomUserDetails.getDetails().getUserId())
        );
    }

    /**
     * 친구 그룹 목록 조회
     *
     * @return 친구 그룹 정보 목록
     */
    public List<FriendGroupDto> findFriendGroups() {
        return friendGroupService.findFriendGroups(
                userService.getUser(CustomUserDetails.getDetails().getUserId())
        );
    }

    /**
     * 친구 그룹 수정
     *
     * @param friendGrpId - 친구 그룹 아이디
     * @param updateDto   - 수정 정보
     */
    @Transactional
    public void updateFriendGroup(Long friendGrpId, FriendGroupUpdateDto updateDto) {
        friendGroupService.updateFriendGroup(
                friendGrpId
                , CustomUserDetails.getDetails().getUserId()
                , updateDto
        );
    }

    /**
     * 친구 그룹 삭제
     *
     * @param friendGrpId - 친구 그룹 아이디
     * @param cascade     - 하위 삭제 여부
     */
    @Transactional
    public void deleteFriendGroup(Long friendGrpId, boolean cascade) {

        String userId = CustomUserDetails.getDetails().getUserId();
        FriendGroup friendGroup = friendGroupService.getFriendGroup(friendGrpId, userId);
        if(cascade) {
            for (Friend friend : friendGroup.getFriends()) {
                friendService.deleteFriend(friend.getFriendId(), userId);
            }
        } else {

            Double order = friendService.getLastOrder(CustomUserDetails.getDetails().getUserId());
            for (Friend friend : friendGroup.getFriends()) {
                friend.update(
                        FriendUpdateDto.builder()
                                .ord(order)
                                .build()
                        , null
                );
                order++;
            }

        }

        friendGroupService.deleteFriendGroup(friendGrpId, userId);

    }

    /**
     * 친구 신청 등록
     *
     * @param createDto - 등록 정보
     * @return 생성된 친구 신청 아이디
     */
    @Transactional
    public Long createFriendRequest(FriendRequestCreateDto createDto) {

        FriendRequest createRequest = friendRequestService.createFriendRequest(
                createDto
                , userService.getUser(CustomUserDetails.getDetails().getUserId())
                , userService.getUser(createDto.getTargetId())
        );

        createNotification(createRequest, NotificationType.REQUEST);

        return createRequest.getFriendReqId();

    }

    private void createNotification(FriendRequest createRequest, NotificationType notiType) {

        FriendRequestNotification createNotification = friendRequestNotificationService.createNotification(
                FriendRequestNotification.builder()
                        .request(createRequest)
                        .notiType(notiType)
                        .build()
        );

        sendRequestToMq(createRequest, createNotification);

    }

    private void sendRequestToMq(
            FriendRequest request
            , FriendRequestNotification notification
    ) {
        rabbitTemplate.convertAndSend(
                rabbitMqProp.getFriend().getExchange()
                , rabbitMqProp.getFriend().getRoutes().get(FriendRoutes.REQUEST.getKey()).getRouting() +
                        (notification.getNotiType().equals(NotificationType.REQUEST)
                                ? request.getTarget().getUserId()
                                : request.getRequester().getUserId())
                , FriendRequestNotificationDto.of(notification)
        );
    }

    /**
     * 친구 신청 목록 조회
     *
     * @return 친구 그룹 정보 목록
     */
    public List<FriendRequestDto> findFriendRequests() {
        return friendRequestService.findFriendRequests(
                CustomUserDetails.getDetails().getUserId()
        );
    }

    /**
     * 친구 신청 승인
     *
     * @param friendReqId - 친구 신청 아이디
     * @param approve     - 승인 여부
     */
    @Transactional
    public void approveFriendRequest(Long friendReqId, boolean approve) {

        FriendRequest request = friendRequestService.getFriendRequest(friendReqId);
        validateRequest(request);

        if(approve) {

            friendService.createFriend(
                    Friend.builder()
                            .user(request.getRequester())
                            .friend(request.getTarget())
                            .ord(friendService.getLastOrder(request.getRequester().getUserId()))
                            .build()
            );

            friendService.createFriend(
                    Friend.builder()
                            .user(request.getTarget())
                            .friend(request.getRequester())
                            .ord(friendService.getLastOrder(request.getTarget().getUserId()))
                            .build()
            );

        }

        createNotification(request, approve ? NotificationType.APPROVE : NotificationType.REJECT);

        friendRequestService.deleteFriendRequest(friendReqId);

    }

    private static void validateRequest(FriendRequest request) {

        String userId = CustomUserDetails.getDetails().getUserId();
        if(!request.getTarget().getUserId().equalsIgnoreCase(userId))
            throw new ResponseException("Requests not assigned to the current user", HttpStatus.BAD_REQUEST);

        if(request.getRequester().getUserId().equalsIgnoreCase(userId))
            throw new ResponseException("Request made by current user", HttpStatus.BAD_REQUEST);

    }

    /**
     * 친구 목록 조회
     *
     * @return 친구 정보 목록
     */
    public List<FriendDto> findFriends() {
        return friendService.findFriends(CustomUserDetails.getDetails().getUserId());
    }

    /**
     * 친구 수정
     *
     * @param friendId  - 친구 아이디
     * @param updateDto - 수정 정보
     */
    @Transactional
    public void updateFriend(Long friendId, FriendUpdateDto updateDto) {
        String userId = CustomUserDetails.getDetails().getUserId();
        friendService.updateFriend(
                friendId
                , userId
                , updateDto
                , ObjectUtils.isEmpty(updateDto.getFriendGrpId())
                        ? null
                        : friendGroupService.getFriendGroup(updateDto.getFriendGrpId(), userId)
        );
    }

    /**
     * 친구 삭제
     *
     * @param friendId - 친구 아이디
     */
    @Transactional
    public void deleteFriend(Long friendId) {
        friendService.deleteFriend(friendId, CustomUserDetails.getDetails().getUserId());
    }

    /**
     * 친구 신청 알림 목록 조회
     */
    public List<FriendRequestNotificationDto> findFriendRequestNotifications() {
        return friendRequestNotificationService.findNotifications(CustomUserDetails.getDetails().getUserId());
    }

    /**
     * 친구 신청 알림 확인
     *
     * @param notiId - 알림 아이디
     */
    @Transactional
    public void checkFriendRequestNotification(Long notiId) {

        FriendRequestNotification notification = friendRequestNotificationService.getNotification(notiId);
        validateNotification(notification);
        notification.check();

    }

    private void validateNotification(FriendRequestNotification notification) {

        String userId = CustomUserDetails.getDetails().getUserId();
        switch (notification.getNotiType()) {

            case REQUEST -> {
                if(!notification.getRequest().getTarget().getUserId().equalsIgnoreCase(userId))
                    throw new ResponseException("The notification not assigned to the current user", HttpStatus.BAD_REQUEST);
            }

            default -> {
                if(!notification.getRequest().getRequester().getUserId().equalsIgnoreCase(userId))
                    throw new ResponseException("The notification not assigned to the current user", HttpStatus.BAD_REQUEST);
            }

        }

    }

    /**
     * 친구 신청 알림 삭제
     *
     * @param notiId - 알림 아이디
     */
    @Transactional
    public void deleteFriendRequestNotification(Long notiId) {

        FriendRequestNotification notification = friendRequestNotificationService.getNotification(notiId);
        validateNotification(notification);
        friendRequestNotificationService.deleteNotification(notiId);

    }

}
