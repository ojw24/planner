package com.ojw.planner.app.planner.goal.domain.dto;

import com.ojw.planner.core.enumeration.planner.goal.GoalType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "목표 조회 DTO")
public class GoalFindDto {

    @NotNull
    @Schema(description = "목표 타입")
    private GoalType goalType;

    @NotNull
    @Schema(description = "조회날짜")
    private LocalDate localDate;

    @Schema(description = "상세 조회 여부")
    private boolean detail;

}
