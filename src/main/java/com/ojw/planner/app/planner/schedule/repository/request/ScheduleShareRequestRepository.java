package com.ojw.planner.app.planner.schedule.repository.request;

import com.ojw.planner.app.planner.schedule.domain.request.ScheduleShareRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleShareRequestRepository extends JpaRepository<ScheduleShareRequest, Long> {

    List<ScheduleShareRequest> findAllByTargetUserId(String userId);

    Optional<ScheduleShareRequest> findByReqIdAndTargetUserId(Long reqId, String userId);

    boolean existsByScheduleScheduleIdAndTargetUserId(Long scheduleId, String userId);

}
