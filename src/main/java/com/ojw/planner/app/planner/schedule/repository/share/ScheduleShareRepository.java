package com.ojw.planner.app.planner.schedule.repository.share;

import com.ojw.planner.app.planner.schedule.domain.share.ScheduleShare;
import com.ojw.planner.app.planner.schedule.repository.share.querydsl.ScheduleShareRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleShareRepository extends JpaRepository<ScheduleShare, ScheduleShare.ScheduleSharePK>, ScheduleShareRepositoryCustom {

    void deleteByScheduleScheduleIdAndUserUserId(Long scheduleId, String userId);

    boolean existsByScheduleScheduleIdAndUserUserId(Long scheduleId, String userId);

}
