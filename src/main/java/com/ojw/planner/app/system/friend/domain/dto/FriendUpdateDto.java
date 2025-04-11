package com.ojw.planner.app.system.friend.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "친구 수정 DTO")
public class FriendUpdateDto {

    @Positive
    @Schema(description = "친구 그룹 아이디")
    private Long friendGrpId;

}
