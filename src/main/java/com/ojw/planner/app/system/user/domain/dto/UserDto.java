package com.ojw.planner.app.system.user.domain.dto;

import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.app.system.user.domain.setting.UserSetting;
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

    @Schema(description = "설정")
    private UserSettingDto setting;

    public static UserDto of(User user) {
        return UserDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .isBanned(user.getIsBanned())
                .regDtm(user.getRegDtm())
                .updtDtm(user.getUpdtDtm())
                .setting(UserSettingDto.of(user.getSetting()))
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

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSettingDto {

        @Schema(description = "친구 신청 알림 여부")
        private Boolean isFriendReqNoti;

        @Schema(description = "일정 공유 신청 알림 여부")
        private Boolean isSchShareReqNoti;

        @Schema(description = "댓글 알림 여부")
        private Boolean isCommentNoti;

        public static UserSettingDto of(UserSetting setting) {
            return UserSettingDto.builder()
                    .isFriendReqNoti(setting.getIsFriendReqNoti())
                    .isSchShareReqNoti(setting.getIsSchShareReqNoti())
                    .isCommentNoti(setting.getIsCommentNoti())
                    .build();
        }

    }

}
