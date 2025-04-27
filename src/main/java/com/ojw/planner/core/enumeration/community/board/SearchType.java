package com.ojw.planner.core.enumeration.community.board;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ojw.planner.core.enumeration.mapper.EnumMapper;
import com.ojw.planner.core.util.EnumUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

import static com.ojw.planner.app.community.board.domain.memo.QBoardMemo.boardMemo;

@Getter
@AllArgsConstructor
public enum SearchType implements EnumMapper {

    TITLE("title", "제목", boardMemo.title::containsIgnoreCase),
    NAME("name", "작성자 이름", boardMemo.user.name::containsIgnoreCase),
    UUID("uuid", "작성자 고유 키", boardMemo.user.uuid::containsIgnoreCase);

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
