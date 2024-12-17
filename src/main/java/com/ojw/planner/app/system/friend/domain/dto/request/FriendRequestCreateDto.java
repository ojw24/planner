package com.ojw.planner.app.system.friend.domain.dto.request;

import com.ojw.planner.app.system.friend.domain.request.FriendRequest;
import com.ojw.planner.app.system.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "친구 신청 등록 DTO")
public class FriendRequestCreateDto {

    @NotBlank
    @Schema(description = "대상자 아이디")
    private String targetId;

    public FriendRequest toEntity(User requester, User target) {
        return FriendRequest.builder()
                .requester(requester)
                .target(target)
                .build();
    }

}
