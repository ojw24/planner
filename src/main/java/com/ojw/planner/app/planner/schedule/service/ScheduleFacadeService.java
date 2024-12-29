package com.ojw.planner.app.planner.schedule.service;

import com.ojw.planner.app.planner.schedule.domain.Schedule;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleCreateDto;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleDto;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleFindDto;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleUpdateDto;
import com.ojw.planner.app.planner.schedule.domain.dto.request.ScheduleShareRequestCreateDto;
import com.ojw.planner.app.planner.schedule.domain.request.ScheduleShareRequest;
import com.ojw.planner.app.planner.schedule.service.request.ScheduleShareRequestService;
import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.app.system.user.domain.security.CustomUserDetails;
import com.ojw.planner.app.system.user.service.UserService;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleFacadeService {

    private final ScheduleService scheduleService;

    private final ScheduleShareRequestService scheduleShareRequestService;

    private final UserService userService;

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
        return scheduleService.findSchedules(
                findDto
                , CustomUserDetails.getDetails().getUserId()
        );
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
            if(!scheduleShareRequestService.checkRequest(scheduleId, targetId)) {
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

        //TODO : 대상자들에게 알림 보내기

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

            scheduleService.createSchedule(
                    ScheduleCreateDto.builder()
                            .name(request.getSchedule().getName())
                            .startDtm(request.getSchedule().getStartDtm())
                            .endDtm(request.getSchedule().getEndDtm())
                            .location(request.getSchedule().getLocation())
                            .build()
                    , null
                    , request.getTarget()
            );

        } else {
            //TODO : 신청자에게 거절 알림 보내기
        }

        scheduleShareRequestService.deleteScheduleShareRequest(reqId);

    }

}
