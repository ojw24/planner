package com.ojw.planner.app.community.board.service.comment.notification;

import com.ojw.planner.app.community.board.domain.comment.notification.BoardCommentNotification;
import com.ojw.planner.app.community.board.domain.dto.comment.notification.BoardCommentNotificationDto;
import com.ojw.planner.app.community.board.repository.comment.notification.BoardCommentNotificationRepository;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardCommentNotificationService {

    private final BoardCommentNotificationRepository notiRepository;

    @Transactional
    public BoardCommentNotification createNotification(BoardCommentNotification notification) {
        return notiRepository.save(notification);
    }

    public List<BoardCommentNotificationDto> findNotifications(String userId) {
        return notiRepository.findAll(userId).stream().map(BoardCommentNotificationDto::of).collect(Collectors.toList());
    }

    public BoardCommentNotification getNotification(Long notiId) {
        return notiRepository.findById(notiId)
                .orElseThrow(() -> new ResponseException(
                        "not exist board comment notification : " + notiId
                        , HttpStatus.NOT_FOUND
                ));
    }

    @Transactional
    public void deleteNotification(Long notiId) {
        notiRepository.deleteById(notiId);
    }

}
