package com.ojw.planner.app.planner.schedule.domain.dto.request;

import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleDto;
import com.ojw.planner.app.planner.schedule.domain.request.ScheduleShareRequest;
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
public class ScheduleShareRequestDto {

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

    @Schema(description = "등록일시")
    private LocalDateTime regDtm;

    @Schema(description = "수정일시")
    private LocalDateTime updtDtm;

    @Schema(description = "일정 정보")
    private ScheduleDto schedule;

    public static ScheduleShareRequestDto of(ScheduleShareRequest request) {
        return ScheduleShareRequestDto.builder()
                .reqId(request.getReqId())
                .requesterId(request.getRequester().getUserId())
                .requesterName(request.getRequester().getName())
                .targetId(request.getTarget().getUserId())
                .targetName(request.getTarget().getName())
                .regDtm(request.getRegDtm())
                .updtDtm(request.getUpdtDtm())
                .schedule(ScheduleDto.of(request.getSchedule()))
                .build();
    }

}
