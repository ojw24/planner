package com.ojw.planner.app.planner.goal.service;

import com.ojw.planner.app.planner.goal.domain.Goal;
import com.ojw.planner.app.planner.goal.domain.dto.GoalCreateDto;
import com.ojw.planner.app.planner.goal.domain.dto.GoalDto;
import com.ojw.planner.app.planner.goal.domain.dto.GoalFindDto;
import com.ojw.planner.app.planner.goal.domain.dto.GoalUpdateDto;
import com.ojw.planner.app.planner.schedule.service.ScheduleService;
import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.app.system.user.domain.security.CustomUserDetails;
import com.ojw.planner.app.system.user.service.UserService;
import com.ojw.planner.core.enumeration.planner.goal.GoalType;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class GoalFacadeService {

    private final GoalService goalService;

    private final ScheduleService scheduleService;

    private final UserService userService;

    /**
     * 목표 등록
     *
     * @param createDto - 등록 정보
     * @return 생성된 목표 아이디
     */
    @Transactional
    public Long createGoal(GoalCreateDto createDto) {

        String userId = CustomUserDetails.getDetails().getUserId();
        User user = userService.getUser(userId);
        Goal createGoal = goalService.createGoal(
                createDto
                , ObjectUtils.isEmpty(createDto.getParentGoalId())
                        ? null
                        : goalService.getGoal(createDto.getParentGoalId(), userId)
                , user
        );

        createSchedule(createDto, user, createGoal);

        return createGoal.getGoalId();

    }

    private void createSchedule(GoalCreateDto createDto, User user, Goal createGoal) {

        if(createGoal.getGoalType().equals(GoalType.DAY)) {

            if(ObjectUtils.isEmpty(createDto.getScheduleCreateDto()))
                throw new ResponseException("schedule create info is empty", HttpStatus.BAD_REQUEST);

            scheduleService.createSchedule(createDto.getScheduleCreateDto(), createGoal, user);

        }

    }

    /**
     * 목표 조회
     *
     * @param goalId - 목표 아이디
     * @param detail - 상세 조회 여부
     * @return 목표 정보
     */
    public GoalDto findGoal(Long goalId, boolean detail) {
        return goalService.findGoal(
                goalId
                , CustomUserDetails.getDetails().getUserId()
                , detail
        );
    }

    /**
     * 목표 조회(조건)
     *
     * @param findDto - 검색 정보
     * @return 목표 정보
     */
    public GoalDto findGoalBy(GoalFindDto findDto) {
        return goalService.findGoalBy(
                findDto
                , CustomUserDetails.getDetails().getUserId()
        );
    }

    /**
     * 목표 수정
     *
     * @param goalId    - 목표 아이디
     * @param updateDto - 수정 정보
     * @return 수정된 목표 아이디
     */
    @Transactional
    public Long updateGoal(Long goalId, GoalUpdateDto updateDto) {

        String userId = CustomUserDetails.getDetails().getUserId();

        Goal updateGoal = goalService.getGoal(goalId, userId);
        goalService.validateAndSetDate(updateGoal, updateDto, userId);
        updateGoal.update(updateDto);
        goalService.validateParent(updateGoal);

        updateSchedule(updateDto, userId, updateGoal);

        return goalId;

    }

    private void updateSchedule(GoalUpdateDto updateDto, String userId, Goal updateGoal) {
        if(updateGoal.getGoalType().equals(GoalType.DAY) && !ObjectUtils.isEmpty(updateDto.getScheduleUpdateDto())) {

            updateDto.getScheduleUpdateDto().setStartDtm(updateDto.getScheduleUpdateDto().getStartDtm().withDayOfYear(updateGoal.getStartDate().getDayOfYear()));
            updateDto.getScheduleUpdateDto().setEndDtm(updateDto.getScheduleUpdateDto().getEndDtm().withDayOfYear(updateGoal.getEndDate().getDayOfYear()));

            scheduleService.updateSchedule(
                    updateGoal.getSchedule().getScheduleId()
                    , userId
                    , updateDto.getScheduleUpdateDto()
            );

        }
    }

    /**
     * 목표 삭제
     *
     * @param goalId - 목표 아이디
     */
    @Transactional
    public void deleteGoal(Long goalId) {
       goalService.deleteGoal(goalId, CustomUserDetails.getDetails().getUserId());
    }

}
