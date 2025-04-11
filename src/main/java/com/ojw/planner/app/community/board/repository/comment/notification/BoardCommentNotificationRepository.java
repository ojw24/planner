package com.ojw.planner.app.community.board.repository.comment.notification;

import com.ojw.planner.app.community.board.domain.comment.notification.BoardCommentNotification;
import com.ojw.planner.app.community.board.repository.comment.notification.querydsl.BoardCommentNotificationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentNotificationRepository extends JpaRepository<BoardCommentNotification, Long>, BoardCommentNotificationRepositoryCustom {
}
