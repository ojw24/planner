package com.ojw.planner.app.community.board.domain.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "게시글 수정 DTO")
public class BoardMemoUpdateDto {

    @Schema(description = "제목")
    private String title;

    @Schema(description = "내용")
    private String content;

}
