package com.ojw.planner.app.planner.schedule.repository.querydsl;

import com.ojw.planner.app.planner.schedule.domain.Schedule;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleFindDto;

import java.util.List;

public interface ScheduleRepositoryCustom {

    List<Schedule> findAll(ScheduleFindDto findDto, String userId);

}
