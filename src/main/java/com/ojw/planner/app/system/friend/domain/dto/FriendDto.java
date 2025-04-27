package com.ojw.planner.app.system.friend.domain.dto;

import com.ojw.planner.app.system.friend.domain.Friend;
import com.ojw.planner.core.util.Utils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendDto {

    @Schema(description = "친구 아이디")
    private Long friendId;

    @Schema(description = "친구 사용자 아이디")
    private String friendUserId;

    @Schema(description = "친구 사용자 이름")
    private String friendUserName;

    public static FriendDto of(Friend friend) {
        return FriendDto.builder()
                .friendId(friend.getFriendId())
                .friendUserId(Utils.maskingId(friend.getFriend().getUserId()))
                .friendUserName(friend.getFriend().getName())
                .build();
    }

}
