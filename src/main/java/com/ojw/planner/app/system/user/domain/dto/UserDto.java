package com.ojw.planner.app.system.user.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ojw.planner.app.system.attachedFile.domain.dto.AttachedFileDto;
import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.app.system.user.domain.setting.UserSetting;
import com.ojw.planner.core.enumeration.system.user.Authority;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

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

    @Schema(description = "관리자 여부")
    private Boolean isAdmin;

    @Schema(description = "등록일시")
    private LocalDateTime regDtm;

    @Schema(description = "수정일시")
    private LocalDateTime updtDtm;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "설정")
    private UserSettingDto setting;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "첨부 파일")
    private AttachedFileDto file;

    public static UserDto of(User user) {
        return of(user, false);
    }

    public static UserDto of(User user, boolean detail) {
        return UserDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .isBanned(user.getIsBanned())
                .isAdmin(
                        !ObjectUtils.isEmpty(user.getRoles())
                                && user.getRoles().stream().anyMatch(r -> r.getRole().getAuthority().equals(Authority.ADMIN))
                )
                .regDtm(user.getRegDtm())
                .updtDtm(user.getUpdtDtm())
                .setting(detail ? UserSettingDto.of(user.getSetting()) : null)
                .file(detail
                        ? ObjectUtils.isEmpty(user.getAttachedFile())
                            ? null
                            : AttachedFileDto.of(user.getAttachedFile())
                        : null
                )
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
