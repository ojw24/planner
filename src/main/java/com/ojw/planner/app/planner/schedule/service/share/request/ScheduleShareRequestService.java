package com.ojw.planner.app.planner.schedule.service.share.request;

import com.ojw.planner.app.planner.schedule.domain.share.request.ScheduleShareRequest;
import com.ojw.planner.app.planner.schedule.repository.share.request.ScheduleShareRequestRepository;
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
    public List<ScheduleShareRequest> createScheduleShareRequests(List<ScheduleShareRequest> requests) {
        return shareRequestRepository.saveAll(requests);
    }

    public List<ScheduleShareRequest> getScheduleShareRequests(String userId) {
        return shareRequestRepository.findAllByTargetUserIdAndIsDeletedIsFalse(userId);
    }

    public ScheduleShareRequest getScheduleShareRequest(Long reqId, String userId) {
        return shareRequestRepository.findByReqIdAndTargetUserIdAndIsDeletedIsFalse(reqId, userId)
                .orElseThrow(() -> new ResponseException("not exist request", HttpStatus.NOT_FOUND));
    }

    public ScheduleShareRequest getScheduleShareRequest(Long reqId) {
        return shareRequestRepository.findById(reqId)
                .orElseThrow(() -> new ResponseException("not exist request", HttpStatus.NOT_FOUND));
    }

    public boolean checkRequest(Long scheduleId, String userId) {
        return shareRequestRepository.existsByScheduleScheduleIdAndTargetUserIdAndIsDeletedIsFalse(scheduleId, userId);
    }

    @Transactional
    public void deleteScheduleShareRequest(Long reqId) {
        getScheduleShareRequest(reqId).delete();
    }

}
