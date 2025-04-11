package com.ojw.planner.app.community.board.repository.comment.notification.querydsl;

import com.ojw.planner.app.community.board.domain.comment.notification.BoardCommentNotification;

import java.util.List;

public interface BoardCommentNotificationRepositoryCustom {

    List<BoardCommentNotification> findAll(String userId);

}
