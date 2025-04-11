package com.ojw.planner.app.community.notice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "공지사항 조회 DTO")
public class NoticeFindDto {

    @Schema(description = "검색어")
    private String searchValue;

}
