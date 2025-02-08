package com.ojw.planner.app.system.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@ToString
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 수정 DTO")
public class UserUpdateDto {

    @Schema(description = "비밀번호")
    private String password;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "설정 수정 정보")
    private UserSettingUpdateDto settingUpdateDto;

    @Schema(description = "첨부파일 아이디(수정 안 할 경우 기존값, 삭제할 경우 null)")
    private Long attcFileId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "사용자 설정 수정 DTO")
    public static class UserSettingUpdateDto {

        @Schema(description = "친구 신청 알림 여부")
        private Boolean isFriendReqNoti;

        @Schema(description = "일정 공유 신청 알림 여부")
        private Boolean isSchShareReqNoti;

        @Schema(description = "댓글 알림 여부")
        private Boolean isCommentNoti;

    }

}
