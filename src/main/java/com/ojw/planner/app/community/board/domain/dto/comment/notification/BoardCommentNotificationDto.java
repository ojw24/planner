package com.ojw.planner.app.community.board.domain.dto.comment.notification;

import com.ojw.planner.app.community.board.domain.comment.notification.BoardCommentNotification;
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
public class BoardCommentNotificationDto {

    @Schema(description = "알림 아이디")
    private Long notiId;

    @Schema(description = "댓글 아이디")
    private Long boardCommentId;

    @Schema(description = "댓글 내용")
    private String content;

    @Schema(description = "작성자 아이디")
    private String writerId;

    @Schema(description = "작성자 이름")
    private String writerName;

    @Schema(description = "확인 여부")
    private Boolean isChecked;

    @Schema(description = "생성 일시")
    private LocalDateTime regDtm;

    public static BoardCommentNotificationDto of(BoardCommentNotification notification) {
        return BoardCommentNotificationDto.builder()
                .notiId(notification.getNotiId())
                .boardCommentId(notification.getComment().getBoardCommentId())
                .content(notification.getComment().getContent())
                .writerId(notification.getComment().getUser().getUserId())
                .writerName(notification.getComment().getUser().getName())
                .isChecked(notification.getIsChecked())
                .regDtm(notification.getRegDtm())
                .build();
    }

}
