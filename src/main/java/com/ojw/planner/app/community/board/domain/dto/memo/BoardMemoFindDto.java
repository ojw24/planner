package com.ojw.planner.app.community.board.domain.dto.memo;

import com.ojw.planner.core.enumeration.community.board.SearchType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "게시글 조회 DTO")
public class BoardMemoFindDto {

    @Schema(description = "검색 조건(제목 / 작성자 이름)", example = "id")
    private SearchType searchType;

    @Schema(description = "검색어")
    private String searchValue;

}
