package com.ojw.planner.app.community.board.domain.dto.comment;

import com.ojw.planner.app.community.board.domain.comment.BoardComment;
import com.ojw.planner.app.community.board.domain.memo.BoardMemo;
import com.ojw.planner.app.system.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "댓글 등록 DTO")
public class BoardCommentCreateDto {

    @NotBlank
    @Schema(description = "내용")
    private String content;

    @Positive
    @Schema(description = "상위 댓글 아이디")
    private Long parentCommentId;

    public BoardComment toEntity(BoardMemo boardMemo, User user, BoardComment parent) {
        return BoardComment.builder()
                .boardMemo(boardMemo)
                .user(user)
                .parent(parent)
                .content(this.content)
                .root(
                        ObjectUtils.isEmpty(parent)
                                ? null
                                : ObjectUtils.isEmpty(parent.getRoot())
                                    ? parent : parent.getRoot()
                )
                .build();
    }

}
