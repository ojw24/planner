package com.ojw.planner.app.community.board.repository.comment.querydsl;

import com.ojw.planner.app.community.board.domain.comment.BoardComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.ojw.planner.app.community.board.domain.comment.QBoardComment.boardComment;
import static com.ojw.planner.app.community.board.domain.memo.QBoardMemo.boardMemo;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class BoardCommentRepositoryCustomImpl implements BoardCommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BoardComment> findAll(Long boardMemoId, Pageable pageable) {

        List<BoardComment> findBoardMemos = queryFactory
                .selectFrom(boardComment)
                .join(boardMemo)
                .on(
                        boardMemo.boardMemoId.eq(boardMemoId)
                        , boardMemo.isDeleted.isFalse()
                        , boardComment.boardMemo.eq(boardMemo)
                )
                .where(
                        boardComment.isDeleted.isFalse()
                        , boardComment.parent.isNull()
                )
                .orderBy(boardComment.regDtm.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(findBoardMemos, pageable, findAllCount(boardMemoId));

    }

    private Long findAllCount(Long boardMemoId) {
        return queryFactory
                .select(boardComment.count())
                .from(boardComment)
                .join(boardMemo)
                .on(
                        boardMemo.boardMemoId.eq(boardMemoId)
                        , boardMemo.isDeleted.isFalse()
                        , boardComment.boardMemo.eq(boardMemo)
                )
                .where(
                        boardComment.isDeleted.isFalse()
                        , boardComment.parent.isNull()
                )
                .fetchOne();
    }

    @Override
    public Optional<BoardComment> find(Long boardMemoId, Long boardCommentId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(boardComment)
                .join(boardMemo)
                .on(
                        boardMemo.boardMemoId.eq(boardMemoId)
                        , boardMemo.isDeleted.isFalse()
                        , boardComment.boardMemo.eq(boardMemo)
                )
                .where(
                        boardComment.boardCommentId.eq(boardCommentId)
                        , boardComment.isDeleted.isFalse()
                )
                .fetchOne());
    }

}
