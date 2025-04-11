package com.ojw.planner.app.planner.schedule.repository;

import com.ojw.planner.app.planner.schedule.domain.Schedule;
import com.ojw.planner.app.planner.schedule.repository.querydsl.ScheduleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {

    Optional<Schedule> findByScheduleIdAndUserUserIdAndIsDeletedIsFalse(Long scheduleId, String userId);

}
