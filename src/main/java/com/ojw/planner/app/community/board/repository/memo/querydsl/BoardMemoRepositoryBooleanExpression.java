package com.ojw.planner.app.community.board.repository.memo.querydsl;

import com.ojw.planner.core.enumeration.community.board.SearchType;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public class BoardMemoRepositoryBooleanExpression {

    protected BooleanExpression containsSearchValue(SearchType searchType, String searchValue) {
        return !ObjectUtils.isEmpty(searchType) && StringUtils.hasText(searchValue)
                ? searchType.search(searchValue)
                : null;
    }

}
