package com.ojw.planner.app.planner.schedule.domain.dto.request.notification;

import com.ojw.planner.app.planner.schedule.domain.request.notification.ScheduleShareRequestNotification;
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
public class ScheduleShareRequestNotificationDto {

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
                .build();
    }

}
