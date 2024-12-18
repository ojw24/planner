package com.ojw.planner.app.community.notice.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.util.StringUtils;

import static com.ojw.planner.app.community.notice.domain.QNotice.notice;

public class NoticeRepositoryBooleanExpression {

    protected BooleanExpression containsSearchValue(String searchValue) {
        return StringUtils.hasText(searchValue)
                ? notice.title.containsIgnoreCase(searchValue)
                : null;
    }

}
