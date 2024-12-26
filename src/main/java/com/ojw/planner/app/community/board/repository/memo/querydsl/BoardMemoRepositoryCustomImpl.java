package com.ojw.planner.app.community.board.repository.memo.querydsl;

import com.ojw.planner.app.community.board.domain.dto.memo.BoardMemoFindDto;
import com.ojw.planner.app.community.board.domain.memo.BoardMemo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.ojw.planner.app.community.board.domain.QBoard.board;
import static com.ojw.planner.app.community.board.domain.memo.QBoardMemo.boardMemo;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class BoardMemoRepositoryCustomImpl
        extends BoardMemoRepositoryBooleanExpression
        implements BoardMemoRepositoryCustom
{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BoardMemo> findAll(Long boardId, BoardMemoFindDto findDto, Pageable pageable) {

        List<BoardMemo> findBoardMemos = queryFactory
                .selectFrom(boardMemo)
                .join(board)
                .on(
                        board.boardId.eq(boardId)
                        , board.isDeleted.isFalse()
                        , boardMemo.board.eq(board)
                )
                .where(
                        containsSearchValue(findDto.getSearchType(), findDto.getSearchValue())
                        , boardMemo.isDeleted.isFalse()
                )
                .orderBy(boardMemo.regDtm.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(findBoardMemos, pageable, findAllCount(boardId, findDto));

    }

    private Long findAllCount(Long boardId, BoardMemoFindDto findDto) {
        return queryFactory
                .select(boardMemo.count())
                .from(boardMemo)
                .join(board)
                .on(
                        board.boardId.eq(boardId)
                        , board.isDeleted.isFalse()
                        , boardMemo.board.eq(board)
                )
                .where(
                        containsSearchValue(findDto.getSearchType(), findDto.getSearchValue())
                        , boardMemo.isDeleted.isFalse()
                )
                .fetchOne();
    }

    @Override
    public Optional<BoardMemo> find(Long boardId, Long boardMemoId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(boardMemo)
                .join(board)
                .on(
                        board.boardId.eq(boardId)
                        , board.isDeleted.isFalse()
                        , boardMemo.board.eq(board)
                )
                .where(
                        boardMemo.boardMemoId.eq(boardMemoId)
                        , boardMemo.isDeleted.isFalse()
                )
                .fetchOne());
    }

}
