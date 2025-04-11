package com.ojw.planner.app.planner.schedule.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "일정 공유 신청 등록 DTO")
public class ScheduleShareRequestCreateDto {

    @NotEmpty
    @Schema(description = "대상자 아이디 리스트")
    private List<String> targetIds;

}
