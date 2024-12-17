package com.ojw.planner.app.system.user.domain.dto;

import com.ojw.planner.core.enumeration.system.user.SearchType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 조회 DTO")
public class UserFindDto {

    @Schema(description = "검색 조건(ID / 이름)", example = "id")
    private SearchType searchType;

    @Schema(description = "검색어")
    private String searchValue;

}
