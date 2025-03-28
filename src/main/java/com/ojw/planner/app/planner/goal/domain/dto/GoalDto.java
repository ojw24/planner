package com.ojw.planner.app.planner.goal.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ojw.planner.app.planner.goal.domain.Goal;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleDto;
import com.ojw.planner.core.enumeration.mapper.EnumValue;
import com.ojw.planner.core.enumeration.planner.goal.GoalType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalDto {

    @Schema(description = "목표 아이디")
    private Long goalId;

    @Schema(description = "사용자 아이디")
    private String userId;

    @Schema(description = "사용자 이름")
    private String userName;

    @Schema(description = "목표명")
    private String name;

    @Schema(description = "달성 여부")
    private Boolean isAchieve;

    @Schema(description = "달성률")
    private Integer achieve;

    @Schema(description = "목표 타입")
    private EnumValue goalType;

    @Schema(description = "목표 시작일")
    private LocalDate startDate;

    @Schema(description = "목표 종료일")
    private LocalDate endDate;

    @Schema(description = "등록일시")
    private LocalDateTime regDtm;

    @Schema(description = "수정일시")
    private LocalDateTime updtDtm;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "하위 목표")
    private List<GoalDto> children;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "일정 정보")
    private ScheduleDto schedule;

    public static GoalDto of(Goal goal, boolean detail) {
        return GoalDto.builder()
                .goalId(goal.getGoalId())
                .userId(goal.getUser().getUserId())
                .userName(goal.getUser().getName())
                .name(goal.getName())
                .isAchieve(goal.getIsAchieve())
                .achieve(getAchieve(goal))
                .goalType(
                        EnumValue.toEnumValue(
                                goal.getGoalType().getClass()
                                , goal.getGoalType().getCode()
                        )
                )
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .regDtm(goal.getRegDtm())
                .updtDtm(goal.getUpdtDtm())
                .children(
                        detail
                                ? ObjectUtils.isEmpty(goal.getChildren())
                                    ? null
                                    : goal.getChildren().stream()
                                        .filter(c -> !c.getIsDeleted())
                                        .map(c -> GoalDto.of(c, true))
                                        .collect(Collectors.toList())
                                : null
                )
                .schedule(
                        ObjectUtils.isEmpty(goal.getSchedule())
                                ? null
                                : ScheduleDto.of(goal.getSchedule())
                )
                .build();
    }

    private static Integer getAchieve(Goal goal) {
        return ObjectUtils.isEmpty(goal.getChildren())
                ? 0
                : goal.getGoalType().equals(GoalType.YEAR)
                    ? getYearAchieve(goal)
                    : Math.round(
                        goal.getChildren().stream()
                                .filter(c -> !c.getIsDeleted())
                                .filter(Goal::getIsAchieve)
                                .toList()
                                .size()
                        / (float)goal.getChildren().stream().filter(c -> !c.getIsDeleted()).toList().size()
                        * 100
                    );
    }

    private static Integer getYearAchieve(Goal goal) {

        AtomicInteger achieve = new AtomicInteger(0);
        goal.getChildren().forEach(c -> {
            if(!c.getIsDeleted()) {
                achieve.set(
                        achieve.get()
                                + Math.round(
                                c.getChildren().stream()
                                        .filter(cc -> !cc.getIsDeleted())
                                        .filter(Goal::getIsAchieve)
                                        .toList()
                                        .size()
                                        / (float)c.getChildren().stream().filter(cc -> !cc.getIsDeleted()).toList().size()
                                        * 100
                                        / goal.getChildren().stream().filter(cc -> !cc.getIsDeleted()).toList().size()
                        )
                );
            }
        });

        return achieve.get();

    }

}
