package com.ojw.planner.app.system.user.domain.dto;

import com.ojw.planner.app.system.user.domain.User;
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
public class UserDto {

    @Schema(description = "사용자 아이디")
    private String userId;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "정지 여부")
    private Boolean isBanned;

    @Schema(description = "등록일시")
    private LocalDateTime regDtm;

    @Schema(description = "수정일시")
    private LocalDateTime updtDtm;

    public static UserDto of(User user) {
        return UserDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .isBanned(user.getIsBanned())
                .regDtm(user.getRegDtm())
                .updtDtm(user.getUpdtDtm())
                .build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSimpleDto {

        @Schema(description = "사용자 아이디")
        private String userId;

        @Schema(description = "이름")
        private String name;

        @Schema(description = "정지 여부")
        protected Boolean isBanned;

    }

}
