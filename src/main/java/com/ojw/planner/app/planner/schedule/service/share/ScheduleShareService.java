package com.ojw.planner.app.planner.schedule.service.share;

import com.ojw.planner.app.planner.schedule.domain.Schedule;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleDto;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleFindDto;
import com.ojw.planner.app.planner.schedule.domain.share.ScheduleShare;
import com.ojw.planner.app.planner.schedule.repository.share.ScheduleShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleShareService {

    private final ScheduleShareRepository shareRepository;

    @Transactional
    public void createScheduleShare(ScheduleShare share) {
        shareRepository.save(share);
    }

    public List<ScheduleDto> findSchedules(ScheduleFindDto findDto, String userId) {
        return getSchedules(findDto, userId).stream()
                .map(s -> ScheduleDto.of(s, true))
                .collect(Collectors.toList());
    }

    public List<Schedule> getSchedules(ScheduleFindDto findDto, String userId) {
        return shareRepository.findAll(findDto, userId);
    }

    public boolean checkSchedule(Long scheduleId, String userId) {
        return shareRepository.existsByScheduleScheduleIdAndUserUserId(scheduleId, userId);
    }

    @Transactional
    public void deleteScheduleShare(Long scheduleId, String userId) {
        shareRepository.deleteByScheduleScheduleIdAndUserUserId(scheduleId, userId);
    }

}
