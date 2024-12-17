package com.ojw.planner.app.system.friend.domain.dto.request;

import com.ojw.planner.app.system.friend.domain.request.FriendRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDto {

    @Schema(description = "친구 신청 아이디")
    private Long friendReqId;

    @Schema(description = "대상자 아이디")
    private String targetId;

    @Schema(description = "대상자 이름")
    private String targetName;

    @Schema(description = "신청 일시")
    private LocalDateTime regDtm;

    @Schema(description = "인바운드 여부")
    private boolean inbound;

    public static FriendRequestDto of(FriendRequest request, String userId) {
        boolean inbound = request.getTarget().getUserId().equalsIgnoreCase(userId);
        return FriendRequestDto.builder()
                .friendReqId(request.getFriendReqId())
                .targetId(
                        inbound
                                ? request.getRequester().getUserId()
                                : request.getTarget().getUserId()
                )
                .targetName(
                        inbound
                                ? request.getRequester().getName()
                                : request.getTarget().getName()
                )
                .regDtm(request.getReqDtm())
                .inbound(inbound)
                .build();
    }

}
