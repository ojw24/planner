package com.ojw.planner.app.planner.schedule.repository.share.querydsl;

import com.ojw.planner.app.planner.schedule.domain.Schedule;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleFindDto;

import java.util.List;

public interface ScheduleShareRepositoryCustom {

    List<Schedule> findAll(ScheduleFindDto findDto, String userId);

}
