package com.ojw.planner.app.community.board.domain.dto.memo;

import com.ojw.planner.app.community.board.domain.memo.BoardMemo;
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
public class BoardMemoDto {

    @Schema(description = "게시글 아이디")
    private Long boardMemoId;

    @Schema(description = "게시판 아이디")
    private Long boardId;

    @Schema(description = "작성자 아이디")
    private String userId;

    @Schema(description = "작성자 이름")
    private String userName;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "내용")
    private String content;

    @Schema(description = "조회수")
    private Long hit;

    @Schema(description = "등록일시")
    private LocalDateTime regDtm;

    @Schema(description = "수정일시")
    private LocalDateTime updtDtm;

    public static BoardMemoDto of(BoardMemo boardMemo) {
        return BoardMemoDto.builder()
                .boardMemoId(boardMemo.getBoardMemoId())
                .boardId(boardMemo.getBoard().getBoardId())
                .userId(boardMemo.getUser().getUserId())
                .userName(boardMemo.getUser().getName())
                .title(boardMemo.getTitle())
                .content(boardMemo.getContent())
                .hit(boardMemo.getHit())
                .regDtm(boardMemo.getRegDtm())
                .updtDtm(boardMemo.getUpdtDtm())
                .build();
    }

}
