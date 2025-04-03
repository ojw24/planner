package com.ojw.planner.app.community.board.repository.comment.querydsl;

import com.ojw.planner.app.community.board.domain.comment.BoardComment;
import com.ojw.planner.app.community.board.domain.comment.QBoardComment;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.ojw.planner.app.community.board.domain.comment.QBoardComment.boardComment;
import static com.ojw.planner.app.community.board.domain.memo.QBoardMemo.boardMemo;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class BoardCommentRepositoryCustomImpl implements BoardCommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BoardComment> findAll(Long boardMemoId, Pageable pageable) {

        QBoardComment child = new QBoardComment("child");

        List<BoardComment> findBoardMemos = queryFactory
                .selectFrom(boardComment)
                .join(boardMemo)
                .on(
                        boardMemo.boardMemoId.eq(boardMemoId)
                        , boardMemo.isDeleted.isFalse()
                        , boardComment.boardMemo.eq(boardMemo)
                )
                .where(
                        boardComment.parent.isNull()
                        , boardComment.isDeleted.isFalse()
                                .or(
                                        JPAExpressions
                                                .selectOne()
                                                .from(child)
                                                .where(
                                                        child.root.eq(boardComment)
                                                        , child.isDeleted.isFalse()
                                                )
                                                .exists()
                                )
                )
                .orderBy(boardComment.regDtm.desc(), boardComment.boardCommentId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(findBoardMemos, pageable, findAllCount(boardMemoId));

    }

    private Long findAllCount(Long boardMemoId) {

        QBoardComment child = new QBoardComment("child");

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
                        boardComment.parent.isNull()
                        , boardComment.isDeleted.isFalse()
                                .or(
                                        JPAExpressions
                                                .selectOne()
                                                .from(child)
                                                .where(
                                                        child.root.eq(boardComment)
                                                        , child.isDeleted.isFalse()
                                                )
                                                .exists()
                                )
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

    @Override
    public Long getOrder(Long boardMemoId, Long boardCommentId, LocalDateTime regDtm) {

        QBoardComment child = new QBoardComment("child");

        return queryFactory
                .select(boardComment.count())
                .from(boardComment)
                .where(
                        boardComment.boardMemo.boardMemoId.eq(boardMemoId)
                        , boardComment.parent.isNull()
                        , boardComment.isDeleted.isFalse()
                                .or(
                                        JPAExpressions
                                                .selectOne()
                                                .from(child)
                                                .where(
                                                        child.root.eq(boardComment)
                                                        , child.isDeleted.isFalse()
                                                )
                                                .exists()
                                )
                        , boardComment.regDtm.gt(regDtm).or(
                                boardComment.regDtm.eq(regDtm).and(
                                        boardComment.boardCommentId.gt(boardCommentId)
                                )
                        )
                )
                .fetchOne();

    }

}
