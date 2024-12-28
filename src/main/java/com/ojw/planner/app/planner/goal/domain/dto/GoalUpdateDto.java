package com.ojw.planner.app.planner.goal.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "목표 수정 DTO")
public class GoalUpdateDto {

    @Schema(description = "목표명")
    private String title;

    @Schema(description = "달성 여부")
    private Boolean isArchive;

    @Schema(description = "목표 시작일")
    private LocalDate startDate;

    @Schema(description = "목표 종료일")
    private LocalDate endDate;

}
