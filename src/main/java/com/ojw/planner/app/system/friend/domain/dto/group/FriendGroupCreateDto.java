package com.ojw.planner.app.system.friend.domain.dto.group;

import com.ojw.planner.app.system.friend.domain.group.FriendGroup;
import com.ojw.planner.app.system.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "친구 그룹 등록 DTO")
public class FriendGroupCreateDto {

    @NotBlank
    @Schema(description = "그룹명")
    private String name;

    public FriendGroup toEntity(User user, Double ord) {
        return FriendGroup.builder()
                .user(user)
                .name(this.name)
                .ord(ord)
                .build();
    }

}
