package com.ojw.planner.app.system.friend.domain.dto.group;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "친구 그룹 수정 DTO")
public class FriendGroupUpdateDto {

    @Schema(description = "그룹명")
    private String name;

    @Positive
    @Schema(description = "순서")
    private Double ord;

}
