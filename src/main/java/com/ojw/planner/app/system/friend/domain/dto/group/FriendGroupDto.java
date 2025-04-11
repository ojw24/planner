package com.ojw.planner.app.system.friend.domain.dto.group;

import com.ojw.planner.app.system.friend.domain.dto.FriendDto;
import com.ojw.planner.app.system.friend.domain.group.FriendGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendGroupDto {

    @Schema(description = "친구 그룹 아이디")
    private Long friendGrpId;

    @Schema(description = "그룹명")
    private String name;

    @Schema(description = "순서")
    private Double ord;

    @Schema(description = "친구 리스트")
    private List<FriendDto> friends;

    public static FriendGroupDto of(FriendGroup friendGroup) {
        return FriendGroupDto.builder()
                .friendGrpId(friendGroup.getFriendGrpId())
                .name(friendGroup.getName())
                .ord(friendGroup.getOrd())
                .friends(
                        ObjectUtils.isEmpty(friendGroup.getFriends())
                                ? null
                                : friendGroup.getFriends().stream()
                                .map(FriendDto::of)
                                .sorted(Comparator.comparing(FriendDto::getFriendUserName))
                                .collect(Collectors.toList())
                )
                .build();
    }

}
