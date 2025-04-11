package com.ojw.planner.app.community.board.repository.comment.notification.querydsl;

import com.ojw.planner.app.community.board.domain.comment.QBoardComment;
import com.ojw.planner.app.community.board.domain.comment.notification.BoardCommentNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ojw.planner.app.community.board.domain.comment.notification.QBoardCommentNotification.boardCommentNotification;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardCommentNotificationRepositoryCustomImpl implements BoardCommentNotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardCommentNotification> findAll(String userId) {

        QBoardComment boardComment = new QBoardComment("boardComment");
        QBoardComment parentComment = new QBoardComment("parentComment");

        return queryFactory
                .selectFrom(boardCommentNotification)
                .join(boardComment)
                .on(boardCommentNotification.comment.eq(boardComment))
                .leftJoin(boardComment.parent, parentComment)
                .on(boardComment.parent.eq(parentComment))
                .where(
                        (
                            parentComment.isNull()
                             .and(boardComment.boardMemo.user.userId.equalsIgnoreCase(userId))
                        )
                        .or(
                            parentComment.isNotNull()
                            .and(parentComment.user.userId.equalsIgnoreCase(userId))
                        )
                )
                .orderBy(boardCommentNotification.regDtm.desc())
                .fetch();

    }

}
