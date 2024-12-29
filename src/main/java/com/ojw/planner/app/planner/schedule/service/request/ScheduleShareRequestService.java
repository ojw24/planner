package com.ojw.planner.app.planner.schedule.service.request;

import com.ojw.planner.app.planner.schedule.domain.request.ScheduleShareRequest;
import com.ojw.planner.app.planner.schedule.repository.request.ScheduleShareRequestRepository;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleShareRequestService {

    private final ScheduleShareRequestRepository shareRequestRepository;

    @Transactional
    public void createScheduleShareRequests(List<ScheduleShareRequest> requests) {
        shareRequestRepository.saveAll(requests);
    }

    public ScheduleShareRequest getScheduleShareRequest(Long reqId, String userId) {
        return shareRequestRepository.findByReqIdAndTargetUserId(reqId, userId)
                .orElseThrow(() -> new ResponseException("not exist request", HttpStatus.NOT_FOUND));
    }

    public boolean checkRequest(Long scheduleId, String userId) {
        return shareRequestRepository.existsByScheduleScheduleIdAndTargetUserId(scheduleId, userId);
    }

    public void deleteScheduleShareRequest(Long reqId) {
        shareRequestRepository.deleteById(reqId);
    }

}
