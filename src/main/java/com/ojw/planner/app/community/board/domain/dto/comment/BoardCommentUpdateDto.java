package com.ojw.planner.app.community.board.domain.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "댓글 수정 DTO")
public class BoardCommentUpdateDto {

    @Schema(description = "내용")
    private String content;

}
