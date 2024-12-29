package com.ojw.planner.app.planner.schedule.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "일정 수정 DTO")
public class ScheduleUpdateDto {

    @Schema(description = "일정명")
    private String name;

    @Schema(description = "일정 시작일시")
    private LocalDateTime startDtm;

    @Schema(description = "일정 종료일시")
    private LocalDateTime endDtm;

    @Schema(description = "장소")
    private String location;

}
