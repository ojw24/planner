package com.ojw.planner.core.enumeration.system.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ojw.planner.core.enumeration.mapper.EnumMapper;
import com.ojw.planner.core.util.EnumUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

import static com.ojw.planner.app.system.user.domain.QUser.user;

@Getter
@AllArgsConstructor
public enum SearchType implements EnumMapper {

    ID("id", "채널 아이디", user.userId::containsIgnoreCase),
    NAME("name", "채널명", user.name::containsIgnoreCase);

    private final String code;

    private final String description;

    private Function<String, BooleanExpression> expression;

    public BooleanExpression search(String searchValue){
        return expression.apply(searchValue);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SearchType ofCode(String code) {
        return EnumUtil.ofCode(SearchType.class, code);
    }

}
