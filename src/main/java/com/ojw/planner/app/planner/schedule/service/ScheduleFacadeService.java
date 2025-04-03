package com.ojw.planner.app.planner.schedule.service;

import com.ojw.planner.app.planner.schedule.domain.Schedule;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleCreateDto;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleDto;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleFindDto;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleUpdateDto;
import com.ojw.planner.app.planner.schedule.domain.dto.request.ScheduleShareRequestCreateDto;
import com.ojw.planner.app.planner.schedule.domain.dto.request.ScheduleShareRequestDto;
import com.ojw.planner.app.planner.schedule.domain.dto.request.notification.ScheduleShareRequestNotificationDto;
import com.ojw.planner.app.planner.schedule.domain.share.ScheduleShare;
import com.ojw.planner.app.planner.schedule.domain.share.request.ScheduleShareRequest;
import com.ojw.planner.app.planner.schedule.domain.share.request.notification.ScheduleShareRequestNotification;
import com.ojw.planner.app.planner.schedule.service.share.ScheduleShareService;
import com.ojw.planner.app.planner.schedule.service.share.request.ScheduleShareRequestService;
import com.ojw.planner.app.planner.schedule.service.share.request.notification.ScheduleShareRequestNotificationService;
import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.app.system.user.domain.security.CustomUserDetails;
import com.ojw.planner.app.system.user.service.UserService;
import com.ojw.planner.config.RabbitMqConfigProperties;
import com.ojw.planner.core.enumeration.common.NotificationType;
import com.ojw.planner.core.util.RabbitMqUtil;
import com.ojw.planner.core.util.ServiceUtil;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleFacadeService {

    private final ScheduleService scheduleService;

    private final ScheduleShareService scheduleShareService;

    private final ScheduleShareRequestService scheduleShareRequestService;

    private final ScheduleShareRequestNotificationService notificationService;

    private final UserService userService;

    private final RabbitTemplate rabbitTemplate;

    private final RabbitMqConfigProperties rabbitMqProp;

    private final RabbitMqUtil rabbitMqUtil;

    /**
     * 일정 등록
     *
     * @param createDto - 등록 정보
     * @return 생성된 일정 아이디
     */
    @Transactional
    public Long createSchedule(ScheduleCreateDto createDto) {
        return scheduleService.createSchedule(
                createDto
                , null
                , userService.getUser(CustomUserDetails.getDetails().getUserId())
        );
    }

    /**
     * 일정 조회
     *
     * @param findDto - 검색 정보
     * @return 일정 정보
     */
    public List<ScheduleDto> findSchedules(ScheduleFindDto findDto) {

        String userId = CustomUserDetails.getDetails().getUserId();
        List<ScheduleDto> schedules = scheduleService.findSchedules(findDto, userId);
        schedules.addAll(scheduleShareService.findSchedules(findDto, userId));

        return schedules;

    }

    /**
     * 일정 수정
     *
     * @param scheduleId - 일정 아이디
     * @param updateDto  - 수정 정보
     * @return 수정된 목표 아이디
     */
    @Transactional
    public Long updateSchedule(Long scheduleId, ScheduleUpdateDto updateDto) {

        Schedule updateSchedule = scheduleService.getSchedule(scheduleId, CustomUserDetails.getDetails().getUserId());
        if(updateSchedule.getGoal() != null)
            throw new ResponseException("This is not a normal request", HttpStatus.BAD_REQUEST);

        ServiceUtil.validateOwner(updateSchedule.getUser().getUserId());
        scheduleService.validateUpdateDto(updateDto, updateSchedule);
        updateSchedule.update(updateDto);

        return scheduleId;

    }

    /**
     * 일정 삭제
     *
     * @param scheduleId - 일정 아이디
     */
    @Transactional
    public void deleteSchedule(Long scheduleId) {

        Schedule deleteSchedule = scheduleService.getSchedule(scheduleId, CustomUserDetails.getDetails().getUserId());
        if(deleteSchedule.getGoal() != null)
            throw new ResponseException("This is not a normal request", HttpStatus.BAD_REQUEST);

        ServiceUtil.validateOwner(deleteSchedule.getUser().getUserId());
        deleteSchedule.delete();

    }

    /**
     * 일정 공유 신청 등록
     *
     * @param scheduleId - 일정 아이디
     * @param createDto  - 등록 정보
     */
    @Transactional
    public void createScheduleShareRequests(
            Long scheduleId
            , ScheduleShareRequestCreateDto createDto
    ) {

        String userId = CustomUserDetails.getDetails().getUserId();
        Schedule schedule = scheduleService.getSchedule(scheduleId, userId);
        User requester = userService.getUser(userId);

        List<ScheduleShareRequest> requests = new ArrayList<>();
        for (String targetId : createDto.getTargetIds()) {
            if(!scheduleShareRequestService.checkRequest(scheduleId, targetId)
                    && !scheduleShareService.checkSchedule(scheduleId, targetId)) {
                requests.add(
                        ScheduleShareRequest.builder()
                                .schedule(schedule)
                                .requester(requester)
                                .target(userService.getUser(targetId))
                                .build()
                );
            }
        }

        scheduleShareRequestService.createScheduleShareRequests(requests);
        createNotifications(requests, NotificationType.REQUEST);

    }

    /**
     * 일정 공유 신청 목록 정보 조회
     */
    public List<ScheduleShareRequestDto> findScheduleShareRequests() {
        return scheduleShareRequestService.getScheduleShareRequests(
                CustomUserDetails.getDetails().getUserId()
        ).stream().map(ScheduleShareRequestDto::of).collect(Collectors.toList());
    }

    private void createNotifications(List<ScheduleShareRequest> createRequests, NotificationType notiType) {
        for (ScheduleShareRequest createRequest : createRequests) {
            createNotification(createRequest, notiType);
        }
    }

    private void createNotification(ScheduleShareRequest request, NotificationType notiType) {

        if(checkUserSetting(request, notiType)) {

            ScheduleShareRequestNotification createNotification = notificationService.createNotification(
                    ScheduleShareRequestNotification.builder()
                            .request(request)
                            .notiType(notiType)
                            .build()
            );

            sendRequestToMq(request, createNotification);

        }

    }

    private boolean checkUserSetting(
            ScheduleShareRequest request
            , NotificationType notiType
    ) {
        return notiType.equals(NotificationType.REQUEST)
                ? request.getTarget().getSetting().getIsSchShareReqNoti()
                : request.getRequester().getSetting().getIsSchShareReqNoti();
    }

    private void sendRequestToMq(ScheduleShareRequest request, ScheduleShareRequestNotification notification) {
        rabbitTemplate.convertAndSend(
                rabbitMqProp.getSchedule().getExchange() +
                        (notification.getNotiType().equals(NotificationType.REQUEST)
                                ? request.getTarget().getUserId()
                                : request.getRequester().getUserId())
                , ""
                , ScheduleShareRequestNotificationDto.of(notification)
        );
    }

    /**
     * 일정 공유 신청 승인
     *
     * @param reqId   - 신청 아이디
     * @param approve - 승인 여부
     */
    @Transactional
    public void approveScheduleShareRequest(Long reqId, boolean approve) {

        String userId = CustomUserDetails.getDetails().getUserId();
        ScheduleShareRequest request = scheduleShareRequestService.getScheduleShareRequest(reqId, userId);

        if(approve) {
            scheduleShareService.createScheduleShare(
                    ScheduleShare.builder()
                            .schedule(request.getSchedule())
                            .user(request.getTarget())
                            .build()
            );
        }

        createNotification(request, approve ? NotificationType.APPROVE : NotificationType.REJECT);
        scheduleShareRequestService.deleteScheduleShareRequest(reqId);

    }

    /**
     * 일정 공유 신청 알림 목록 조회
     */
    public List<ScheduleShareRequestNotificationDto> findScheduleShareRequestNotifications() {
        return notificationService.findNotifications(CustomUserDetails.getDetails().getUserId());
    }

    /**
     * 일정 공유 신청 알림 확인
     *
     * @param notiId - 알림 아이디
     */
    @Transactional
    public void checkScheduleShareRequestNotification(Long notiId) {

        ScheduleShareRequestNotification notification = notificationService.getNotification(notiId);
        validateNotification(notification);
        notification.check();

    }

    private void validateNotification(ScheduleShareRequestNotification notification) {

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
     * 일정 공유 신청 알림 삭제
     *
     * @param notiId - 알림 아이디
     */
    @Transactional
    public void deleteScheduleShareRequestNotification(Long notiId) {

        ScheduleShareRequestNotification notification = notificationService.getNotification(notiId);
        validateNotification(notification);
        notificationService.deleteNotification(notiId);

    }

    /**
     * 일정 공유 삭제
     *
     * @param scheduleId - 일정 아이디
     */
    @Transactional
    public void deleteScheduleShare(Long scheduleId) {

        String userId = CustomUserDetails.getDetails().getUserId();
        scheduleShareService.deleteScheduleShare(scheduleId, userId);

    }

    /**
     * MQ 바인딩
     *
     * @param uuid - uuid(queue name)
     */
    public void declareBinding(String uuid) {
        rabbitMqUtil.declareBinding(
                rabbitMqProp.getSchedule().getExchange() + CustomUserDetails.getDetails().getUserId()
                , uuid
        );
    }

}
