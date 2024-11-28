package com.ojw.planner.app.system.user.repository.querydsl;

import com.ojw.planner.core.enumeration.system.user.SearchType;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public class UserRepositoryBooleanExpression {

    protected BooleanExpression containsSearchValue(SearchType searchType, String searchValue) {
        return !ObjectUtils.isEmpty(searchType) && StringUtils.hasText(searchValue)
                ? searchType.search(searchValue)
                : null;
    }

}
