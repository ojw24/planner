package com.ojw.planner.app.community.board.domain.dto.memo;

import com.ojw.planner.app.community.board.domain.Board;
import com.ojw.planner.app.community.board.domain.memo.BoardMemo;
import com.ojw.planner.app.system.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "게시글 등록 DTO")
public class BoardMemoCreateDto {

    @NotBlank
    @Schema(description = "제목")
    private String title;

    @NotBlank
    @Schema(description = "내용")
    private String content;

    public BoardMemo toEntity(Board board, User user) {
        return BoardMemo.builder()
                .board(board)
                .user(user)
                .title(this.title)
                .content(this.content)
                .build();
    }

}
