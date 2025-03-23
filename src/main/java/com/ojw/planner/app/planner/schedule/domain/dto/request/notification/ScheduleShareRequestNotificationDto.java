package com.ojw.planner.app.planner.schedule.domain.dto.request.notification;

import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleDto;
import com.ojw.planner.app.planner.schedule.domain.share.request.notification.ScheduleShareRequestNotification;
import com.ojw.planner.core.domain.Notification;
import com.ojw.planner.core.enumeration.common.NotificationDiv;
import com.ojw.planner.core.enumeration.mapper.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleShareRequestNotificationDto implements Notification {

    @Schema(description = "알림 아이디")
    private Long notiId;

    @Schema(description = "신청 아이디")
    private Long reqId;

    @Schema(description = "신청자 아이디")
    private String requesterId;

    @Schema(description = "신청자 이름")
    private String requesterName;

    @Schema(description = "대상자 아이디")
    private String targetId;

    @Schema(description = "대상자 이름")
    private String targetName;

    @Schema(description = "알림 타입")
    private EnumValue notiType;

    @Schema(description = "확인 여부")
    private Boolean isChecked;

    @Schema(description = "생성 일시")
    private LocalDateTime regDtm;

    @Schema(description = "일정 정보")
    private ScheduleDto schedule;

    @Schema(description = "알림 구분")
    private NotificationDiv notification;

    public static ScheduleShareRequestNotificationDto of(ScheduleShareRequestNotification notification) {
        return ScheduleShareRequestNotificationDto.builder()
                .notiId(notification.getNotiId())
                .reqId(notification.getRequest().getReqId())
                .requesterName(notification.getRequest().getRequester().getName())
                .targetId(notification.getRequest().getTarget().getUserId())
                .targetName(notification.getRequest().getTarget().getName())
                .notiType(
                        EnumValue.toEnumValue(
                                notification.getNotiType().getClass()
                                , notification.getNotiType().getCode()
                        )
                )
                .isChecked(notification.getIsChecked())
                .regDtm(notification.getRegDtm())
                .schedule(ScheduleDto.of(notification.getRequest().getSchedule()))
                .notification(NotificationDiv.SCHEDULE_SHARE_REQUEST)
                .build();
    }

}
