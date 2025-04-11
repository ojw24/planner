package com.ojw.planner.app.community.board.domain.dto.comment;

import com.ojw.planner.app.community.board.domain.comment.BoardComment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardCommentDto {

    @Schema(description = "댓글 아이디")
    private Long boardCommentId;

    @Schema(description = "게시글 아이디")
    private Long boardMemoId;

    @Schema(description = "작성자 아이디")
    private String userId;

    @Schema(description = "작성자 이름")
    private String userName;

    @Schema(description = "내용")
    private String content;

    @Schema(description = "등록일시")
    private LocalDateTime regDtm;

    @Schema(description = "수정일시")
    private LocalDateTime updtDtm;
    
    @Schema(description = "삭제여부")
    private Boolean isDeleted;

    @Schema(description = "하위 댓글")
    private List<BoardCommentDto> children;

    public static BoardCommentDto of(BoardComment boardComment) {
        return BoardCommentDto.builder()
                .boardCommentId(boardComment.getBoardCommentId())
                .boardMemoId(boardComment.getBoardMemo().getBoardMemoId())
                .userId(boardComment.getUser().getUserId())
                .userName(boardComment.getUser().getName())
                .content(boardComment.getContent())
                .regDtm(boardComment.getRegDtm())
                .updtDtm(boardComment.getUpdtDtm())
                .isDeleted(boardComment.getIsDeleted())
                .children(
                        ObjectUtils.isEmpty(boardComment.getChildren())
                                ? null
                                : boardComment.getChildren().stream()
                                    .filter(BoardCommentDto::checkChildren)
                                    .map(BoardCommentDto::of)
                                    .sorted(Comparator.comparing(BoardCommentDto::getRegDtm))
                                    .collect(Collectors.toList())
                )
                .build();
    }

    private static boolean checkChildren(BoardComment boardComment) {

        if (!ObjectUtils.isEmpty(boardComment.getChildren())) {

            for (BoardComment child : boardComment.getChildren()) {
                if (checkChildren(child)) {
                    return true;
                }
            }

            return false; // 모든 children이 true이면 false 반환

        }

        return !boardComment.getIsDeleted(); // 리프 노드면 flag 값 반환

    }

}
