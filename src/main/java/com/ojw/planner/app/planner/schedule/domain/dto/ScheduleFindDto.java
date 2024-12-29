package com.ojw.planner.app.planner.schedule.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "일정 조회 DTO")
public class ScheduleFindDto {

    @NotNull
    @Schema(description = "조회날짜")
    private LocalDate localDate;

}
