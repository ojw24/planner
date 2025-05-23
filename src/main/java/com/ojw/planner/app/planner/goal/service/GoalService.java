package com.ojw.planner.app.planner.goal.service;

import com.ojw.planner.app.planner.goal.domain.Goal;
import com.ojw.planner.app.planner.goal.domain.dto.GoalCreateDto;
import com.ojw.planner.app.planner.goal.domain.dto.GoalDto;
import com.ojw.planner.app.planner.goal.domain.dto.GoalFindDto;
import com.ojw.planner.app.planner.goal.domain.dto.GoalUpdateDto;
import com.ojw.planner.app.planner.goal.repository.GoalRepository;
import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.core.domain.DatePeriod;
import com.ojw.planner.core.enumeration.planner.goal.GoalType;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class GoalService {

    private final GoalRepository goalRepository;

    @Transactional
    public Goal createGoal(GoalCreateDto createDto, Goal parent, User user) {
        validateCreateDto(createDto, parent, user);
        return goalRepository.save(createDto.toEntity(user, parent));
    }

    private void validateCreateDto(GoalCreateDto createDto, Goal parent, User user) {
        validateAndSetDate(createDto, user.getUserId());
        validateParent(createDto, parent, user);
    }

    private void validateParent(GoalCreateDto createDto, Goal parent, User user) {

        switch (createDto.getGoalType()) {

            case YEAR -> {
                if(parent != null) throw new ResponseException("The year goal can't have parent", HttpStatus.BAD_REQUEST);
            }

            case MONTH -> {

                checkParent(parent, user);

                if(!parent.getGoalType().equals(GoalType.YEAR))
                    throw new ResponseException("parent's goal type is not year", HttpStatus.BAD_REQUEST);

                if(createDto.getStartDate().getYear() != parent.getStartDate().getYear())
                    throw new ResponseException("not the current year's month", HttpStatus.BAD_REQUEST);

            }

            case DAY -> {

                checkParent(parent, user);

                if(!parent.getGoalType().equals(GoalType.MONTH))
                    throw new ResponseException("parent's goal type is not month", HttpStatus.BAD_REQUEST);

                if(!validateDay(createDto, parent))
                    throw new ResponseException("not the current month's day", HttpStatus.BAD_REQUEST);

            }

        }

    }

    private static boolean validateDay(GoalCreateDto createDto, Goal parent) {

        LocalDate start = createDto.getStartDate();
        LocalDate parentStart = parent.getStartDate();
        LocalDate parentEnd = parent.getEndDate();

        return (start.isEqual(parentStart) || start.isAfter(parentStart))
                && (start.isEqual(parentEnd) || start.isBefore(parentEnd));

    }

    private static void checkParent(Goal parent, User user) {
        if(parent == null) throw new ResponseException("The parent goal can't be null", HttpStatus.BAD_REQUEST);

        if(!parent.getUser().getUserId().equalsIgnoreCase(user.getUserId()))
            throw new ResponseException("The parent goal is not the current user's goal", HttpStatus.BAD_REQUEST);
    }

    /**
     *
     * 목표 타입
     * -> 연 : 시작일의 해당 년도 1일 ~ 마지막일로 기간 세팅
     * -> 월 : 시작일의 해당 월 1일 ~ 마지막일로 기간 세팅
     * -> 일 : 그대로 세팅
     */
    private void validateAndSetDate(GoalCreateDto createDto, String userId) {

        if(createDto.getStartDate().isAfter(createDto.getEndDate()))
            throw new ResponseException("종료일은 시작일 이후여야 합니다.", HttpStatus.BAD_REQUEST);

        switch (createDto.getGoalType()) {

            case YEAR -> {
                createDto.setStartDate(createDto.getStartDate().withDayOfYear(1));
                createDto.setEndDate(createDto.getStartDate().withDayOfYear(createDto.getStartDate().lengthOfYear()));
            }

            case MONTH -> {
                createDto.setStartDate(createDto.getStartDate().withDayOfMonth(1));
                createDto.setEndDate(createDto.getStartDate().withDayOfMonth(createDto.getStartDate().lengthOfMonth()));
            }

        }

        if(checkDate(createDto.getGoalType(), createDto.getStartDate(), createDto.getEndDate(), userId))
            throw new ResponseException("해당 기간에 중복된 목표가 있습니다.", HttpStatus.BAD_REQUEST);

    }

    private boolean checkDate(GoalType goalType, LocalDate startDate, LocalDate endDate, String userId) {
        return checkDate(goalType, startDate, endDate, userId, null);
    }

    private boolean checkDate(GoalType goalType, LocalDate startDate, LocalDate endDate, String userId, Long goalId) {
        return goalType != GoalType.DAY
                && goalRepository.checkDuplicate(goalType, new DatePeriod(startDate, endDate), userId, goalId) > 0;
    }

    public GoalDto findGoal(Long goalId, String userId, boolean detail) {
        return GoalDto.of(getGoal(goalId, userId), detail);
    }

    public Goal getGoal(Long goalId, String userId) {
        return goalRepository.findByGoalIdAndUserUserIdAndIsDeletedIsFalse(goalId, userId)
                .orElseThrow(() -> new ResponseException("not exist goal", HttpStatus.NOT_FOUND));
    }

    public GoalDto findGoalBy(GoalFindDto findDto, String userId) {
        Goal goal = getGoalBy(findDto, userId);
        return ObjectUtils.isEmpty(goal) ? null : GoalDto.of(goal, findDto.isDetail());
    }

    public Goal getGoalBy(GoalFindDto findDto, String userId) {
        switch (findDto.getGoalType()) {

            case YEAR, MONTH -> {
                return goalRepository.get(findDto, userId);
            }

            default -> {
                return null;
            }

        }
    }

    public void validateAndSetDate(Goal goal, GoalUpdateDto updateDto, String userId) {

        LocalDate startDate = updateDto.getStartDate() == null
                ? goal.getStartDate() : updateDto.getStartDate();

        LocalDate endDate = updateDto.getEndDate() == null
                ? goal.getEndDate() : updateDto.getEndDate();

        if(startDate.isAfter(endDate))
            throw new ResponseException("종료일은 시작일 이후여야 합니다.", HttpStatus.BAD_REQUEST);

        switch (goal.getGoalType()) {

            case YEAR, MONTH -> {
                updateDto.setStartDate(null);
                updateDto.setEndDate(null);
            }

        }

        startDate = updateDto.getStartDate() == null
                ? goal.getStartDate() : updateDto.getStartDate();

        endDate = updateDto.getEndDate() == null
                ? goal.getEndDate() : updateDto.getEndDate();

        if(checkDate(goal.getGoalType(), startDate, endDate, userId, goal.getGoalId()))
            throw new ResponseException("해당 기간에 중복된 목표가 있습니다.", HttpStatus.BAD_REQUEST);

    }

    @Transactional
    public void validateParent(Goal goal, GoalUpdateDto updateDto) {

        switch (goal.getGoalType()) {

            case DAY -> {

                if(!validateDay(goal))
                    throw new ResponseException("not the current month's day", HttpStatus.BAD_REQUEST);

            }

        }

        Goal parent = goal.getParent();
        if(parent != null && updateDto.getIsArchive() != null) {

            if(updateDto.getIsArchive()) {

                boolean achieve = goal.getParent().getChildren().stream().allMatch(Goal::getIsAchieve);
                if(achieve) {
                    parent.update(GoalUpdateDto.builder().isArchive(true).build());
                }

            } else if(parent.getIsAchieve()) {
                parent.update(GoalUpdateDto.builder().isArchive(false).build());
            }

        }

    }

    private static boolean validateDay(Goal goal) {

        LocalDate start = goal.getStartDate();
        LocalDate parentStart = goal.getParent().getStartDate();
        LocalDate parentEnd = goal.getParent().getEndDate();

        return (start.isEqual(parentStart) || start.isAfter(parentStart))
                && (start.isEqual(parentEnd) || start.isBefore(parentEnd));

    }

    @Transactional
    public void deleteGoal(Long goalId, String userId) {
        getGoal(goalId, userId).delete();
    }

}
