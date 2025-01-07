package com.ojw.planner.app.planner.schedule.service.request.notification;

import com.ojw.planner.app.planner.schedule.domain.dto.request.notification.ScheduleShareRequestNotificationDto;
import com.ojw.planner.app.planner.schedule.domain.request.notification.ScheduleShareRequestNotification;
import com.ojw.planner.app.planner.schedule.repository.request.notification.ScheduleShareRequestNotificationRepository;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleShareRequestNotificationService {

    private final ScheduleShareRequestNotificationRepository notiRepository;

    @Transactional
    public ScheduleShareRequestNotification createNotification(ScheduleShareRequestNotification notification) {
        return notiRepository.save(notification);
    }

    public List<ScheduleShareRequestNotificationDto> findNotifications(String userId) {
        return notiRepository.findAll(userId).stream().map(ScheduleShareRequestNotificationDto::of).collect(Collectors.toList());
    }

    public ScheduleShareRequestNotification getNotification(Long notiId) {
        return notiRepository.findById(notiId)
                .orElseThrow(() -> new ResponseException(
                        "not exist schedule share request notification : " + notiId
                        , HttpStatus.NOT_FOUND
                ));
    }

    @Transactional
    public void deleteNotification(Long notiId) {
        notiRepository.deleteById(notiId);
    }

}
