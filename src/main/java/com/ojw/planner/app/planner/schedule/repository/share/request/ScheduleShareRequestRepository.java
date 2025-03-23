package com.ojw.planner.app.planner.schedule.repository.share.request;

import com.ojw.planner.app.planner.schedule.domain.share.request.ScheduleShareRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleShareRequestRepository extends JpaRepository<ScheduleShareRequest, Long> {

    List<ScheduleShareRequest> findAllByTargetUserIdAndIsDeletedIsFalse(String userId);

    Optional<ScheduleShareRequest> findByReqIdAndTargetUserIdAndIsDeletedIsFalse(Long reqId, String userId);

    boolean existsByScheduleScheduleIdAndTargetUserIdAndIsDeletedIsFalse(Long scheduleId, String userId);

}
