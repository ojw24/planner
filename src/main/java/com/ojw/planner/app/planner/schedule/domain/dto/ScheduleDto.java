package com.ojw.planner.app.planner.schedule.domain.dto;

import com.ojw.planner.app.planner.schedule.domain.Schedule;
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
public class ScheduleDto {

    @Schema(description = "일정 아이디")
    private Long scheduleId;

    @Schema(description = "사용자 아이디")
    private String userId;

    @Schema(description = "사용자 이름")
    private String userName;

    @Schema(description = "일정명")
    private String name;

    @Schema(description = "목표 시작일시")
    private LocalDateTime startDtm;

    @Schema(description = "목표 종료일시")
    private LocalDateTime endDtm;

    @Schema(description = "장소")
    private String location;

    @Schema(description = "등록일시")
    private LocalDateTime regDtm;

    @Schema(description = "수정일시")
    private LocalDateTime updtDtm;

    public static ScheduleDto of(Schedule schedule) {
        return ScheduleDto.builder()
                .scheduleId(schedule.getScheduleId())
                .userId(schedule.getUser().getUserId())
                .userName(schedule.getUser().getName())
                .name(schedule.getName())
                .startDtm(schedule.getStartDtm())
                .endDtm(schedule.getEndDtm())
                .location(schedule.getLocation())
                .regDtm(schedule.getRegDtm())
                .updtDtm(schedule.getUpdtDtm())
                .build();
    }
    
}
