package com.ojw.planner.app.planner.goal.domain.dto;

import com.ojw.planner.app.planner.goal.domain.Goal;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleCreateDto;
import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.core.enumeration.planner.goal.GoalType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "목표 등록 DTO")
public class GoalCreateDto {

    @Positive
    @Schema(description = "상위 목표 아이디")
    private Long parentGoalId;

    @NotBlank
    @Schema(description = "목표명")
    private String name;

    @NotNull
    @Schema(description = "목표 타입", example = "goal_type_001")
    private GoalType goalType;

    @NotNull
    @Schema(description = "목표 시작일")
    private LocalDate startDate;

    @NotNull
    @Schema(description = "목표 종료일")
    private LocalDate endDate;

    @Schema(description = "일정 등록 정보")
    private @Valid ScheduleCreateDto scheduleCreateDto;

    public Goal toEntity(User user, Goal parent) {
        return Goal.builder()
                .user(user)
                .parent(parent)
                .name(this.name)
                .goalType(this.goalType)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .build();
    }

}
