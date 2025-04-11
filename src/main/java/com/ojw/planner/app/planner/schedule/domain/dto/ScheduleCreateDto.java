package com.ojw.planner.app.planner.schedule.domain.dto;

import com.ojw.planner.app.planner.goal.domain.Goal;
import com.ojw.planner.app.planner.schedule.domain.Schedule;
import com.ojw.planner.app.system.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "일정 등록 DTO")
public class ScheduleCreateDto {

    @NotBlank
    @Schema(description = "일정명")
    private String name;

    @NotNull
    @Schema(description = "일정 시작일시")
    private LocalDateTime startDtm;

    @NotNull
    @Schema(description = "일정 종료일시")
    private LocalDateTime endDtm;

    @Schema(description = "장소")
    private String location;

    public Schedule toEntity(User user, Goal goal) {
        return Schedule.builder()
                .user(user)
                .goal(goal)
                .name(this.name)
                .startDtm(this.startDtm)
                .endDtm(this.endDtm)
                .location(this.location)
                .build();
    }

}
