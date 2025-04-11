package com.ojw.planner.app.planner.schedule.service;

import com.ojw.planner.app.planner.goal.domain.Goal;
import com.ojw.planner.app.planner.schedule.domain.Schedule;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleCreateDto;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleDto;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleFindDto;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleUpdateDto;
import com.ojw.planner.app.planner.schedule.repository.ScheduleRepository;
import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.core.enumeration.planner.goal.GoalType;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public Long createSchedule(ScheduleCreateDto createDto, Goal goal, User user) {
        validateCreateDto(createDto, goal, user);
        return scheduleRepository.save(createDto.toEntity(user, goal)).getScheduleId();
    }

    private void validateCreateDto(ScheduleCreateDto createDto, Goal goal, User user) {
        validateGoal(createDto, goal, user);
        validateDate(createDto.getStartDtm(), createDto.getEndDtm());
    }

    public void validateGoal(ScheduleCreateDto createDto, Goal goal, User user) {

        if(goal != null) {

            if(!goal.getUser().getUserId().equalsIgnoreCase(user.getUserId()))
                throw new ResponseException("not the current user's goal", HttpStatus.BAD_REQUEST);

            if(!goal.getGoalType().equals(GoalType.DAY))
                throw new ResponseException("goal type is not day", HttpStatus.BAD_REQUEST);

            createDto.setStartDtm(createDto.getStartDtm().withDayOfYear(goal.getStartDate().getDayOfYear()));
            createDto.setEndDtm(createDto.getEndDtm().withDayOfYear(goal.getEndDate().getDayOfYear()));

        }

    }

    private void validateDate(LocalDateTime startDtm, LocalDateTime endDtm) {

        if(startDtm.isAfter(endDtm))
            throw new ResponseException("end dtm must be after start dtm", HttpStatus.BAD_REQUEST);

    }

    public List<ScheduleDto> findSchedules(ScheduleFindDto findDto, String userId) {
        return getSchedules(findDto, userId).stream().map(ScheduleDto::of).collect(Collectors.toList());
    }

    public List<Schedule> getSchedules(ScheduleFindDto findDto, String userId) {
        return scheduleRepository.findAll(findDto, userId);
    }

    public Schedule getSchedule(Long scheduleId, String userId) {
        return scheduleRepository.findByScheduleIdAndUserUserIdAndIsDeletedIsFalse(scheduleId, userId)
                .orElseThrow(() -> new ResponseException("not exist schedule", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public Long updateSchedule(Long scheduleId, String userId, ScheduleUpdateDto updateDto) {

        Schedule updateSchedule = getSchedule(scheduleId, userId);
        validateUpdateDto(updateDto, updateSchedule);
        updateSchedule.update(updateDto);

        return scheduleId;

    }

    public void validateUpdateDto(ScheduleUpdateDto updateDto, Schedule schedule) {

        LocalDateTime startDtm = updateDto.getStartDtm() == null
                ? schedule.getStartDtm() : updateDto.getStartDtm();

        LocalDateTime endDtm = updateDto.getEndDtm() == null
                ? schedule.getEndDtm() : updateDto.getEndDtm();

        validateDate(startDtm, endDtm);

    }

}
