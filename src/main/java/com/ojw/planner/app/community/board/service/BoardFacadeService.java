package com.ojw.planner.app.community.board.service;

import com.ojw.planner.app.community.board.domain.comment.BoardComment;
import com.ojw.planner.app.community.board.domain.comment.notification.BoardCommentNotification;
import com.ojw.planner.app.community.board.domain.dto.comment.BoardCommentCreateDto;
import com.ojw.planner.app.community.board.domain.dto.comment.BoardCommentDto;
import com.ojw.planner.app.community.board.domain.dto.comment.BoardCommentUpdateDto;
import com.ojw.planner.app.community.board.domain.dto.comment.notification.BoardCommentNotificationDto;
import com.ojw.planner.app.community.board.domain.dto.memo.BoardMemoCreateDto;
import com.ojw.planner.app.community.board.domain.dto.memo.BoardMemoUpdateDto;
import com.ojw.planner.app.community.board.domain.memo.BoardMemo;
import com.ojw.planner.app.community.board.domain.memo.redis.CachedBoardMemo;
import com.ojw.planner.app.community.board.service.comment.BoardCommentService;
import com.ojw.planner.app.community.board.service.comment.notification.BoardCommentNotificationService;
import com.ojw.planner.app.community.board.service.memo.BoardMemoService;
import com.ojw.planner.app.community.board.service.memo.redis.CachedBoardMemoService;
import com.ojw.planner.app.system.user.domain.security.CustomUserDetails;
import com.ojw.planner.app.system.user.service.UserService;
import com.ojw.planner.config.RabbitMqConfigProperties;
import com.ojw.planner.core.enumeration.inner.BoardRoutes;
import com.ojw.planner.core.util.ServiceUtil;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardFacadeService {

    private final BoardService boardService;

    private final BoardMemoService boardMemoService;

    private final BoardCommentService boardCommentService;

    private final CachedBoardMemoService cachedBoardMemoService;

    private final BoardCommentNotificationService notificationService;

    private final UserService userService; //TODO : AOP 대체?

    private final RabbitTemplate rabbitTemplate;

    private final RabbitMqConfigProperties rabbitMqProp;

    /**
     * 게시글 등록
     *
     * @param boardId   - 게시판 아이디
     * @param createDto - 등록 정보
     * @return 생성된 게시글 아이디
     */
    @Transactional
    public Long createBoardMemo(Long boardId, BoardMemoCreateDto createDto) {

        BoardMemo createMemo = boardMemoService.saveBoardMemo(
                createDto.toEntity(
                        boardService.getBoard(boardId)
                        , userService.getUser(CustomUserDetails.getDetails().getUserId())
                )
        );

        cachedBoardMemoService.saveBoardMemo(CachedBoardMemo.of(createMemo));

        return createMemo.getBoardMemoId();

    }

    /**
     * 게시글 상세 조회
     *
     * @param boardId     - 게시판 아이디
     * @param boardMemoId - 게시글 아이디
     * @return 게시글 상세 정보
     */
    public CachedBoardMemo findBoardMemo(Long boardId, Long boardMemoId) {

        CachedBoardMemo boardMemo = cachedBoardMemoService.getBoardMemo(boardMemoId)
                        .orElseGet(() -> {
                            CachedBoardMemo cache = CachedBoardMemo.of(boardMemoService.getBoardMemo(boardId, boardMemoId));
                            cachedBoardMemoService.saveBoardMemo(cache);
                            return cache;
                        });

        boardMemo.setHit(cachedBoardMemoService.plusHit(boardMemoId, boardMemo.getHit()));
        return boardMemo;

    }

    /**
     * 게시글 수정
     *
     * @param boardId     - 게시판 아이디
     * @param boardMemoId - 게시글 아이디
     * @param updateDto   - 수정 정보
     * @return 수정된 게시글 아이디
     */
    @Transactional
    public Long updateBoardMemo(Long boardId, Long boardMemoId, BoardMemoUpdateDto updateDto) {

        BoardMemo updateMemo = boardMemoService.getBoardMemo(boardId, boardMemoId);
        ServiceUtil.validateOwner(updateMemo.getUser().getUserId());
        updateMemo.update(updateDto);
        cachedBoardMemoService.saveBoardMemo(CachedBoardMemo.of(updateMemo));

        return updateMemo.getBoardMemoId();

    }

    /**
     * 게시글 삭제
     *
     * @param boardId     - 게시판 아이디
     * @param boardMemoId - 게시글 아이디
     */
    @Transactional
    public void deleteBoardMemo(Long boardId, Long boardMemoId) {
        boardMemoService.deleteBoardMemo(boardId, boardMemoId);
        cachedBoardMemoService.deleteBoardMemo(boardMemoId);
    }

    /**
     * 댓글 등록
     *
     * @param boardId     - 게시판 아이디
     * @param boardMemoId - 게시글 아이디
     * @param createDto   - 등록 정보
     * @return 생성된 댓글 아이디
     */
    @Transactional
    public Long createBoardComment(
            Long boardId
            , Long boardMemoId
            , BoardCommentCreateDto createDto
    ) {

        BoardComment createComment = boardCommentService.saveBoardComment(
                createDto.toEntity(
                        boardMemoService.getBoardMemo(boardId, boardMemoId)
                        , userService.getUser(CustomUserDetails.getDetails().getUserId())
                        , ObjectUtils.isEmpty(createDto.getParentCommentId())
                                ? null
                                : boardCommentService.getBoardComment(boardMemoId, createDto.getParentCommentId())
                )
        );

        createNotification(createComment);

        return createComment.getBoardCommentId();

    }

    private void createNotification(BoardComment createComment) {

        BoardCommentNotification createNotification = notificationService.createNotification(
                BoardCommentNotification.builder()
                        .comment(createComment)
                        .build()
        );

        sendRequestToMq(createComment, createNotification);

    }

    private void sendRequestToMq(
            BoardComment comment
            , BoardCommentNotification notification
    ) {
        rabbitTemplate.convertAndSend(
                rabbitMqProp.getBoard().getExchange()
                , rabbitMqProp.getBoard().getRoutes().get(BoardRoutes.COMMENT.getKey()).getRouting() +
                        (ObjectUtils.isEmpty(comment.getParent())
                                ? comment.getBoardMemo().getUser().getUserId()
                                : comment.getParent().getUser().getUserId())
                , BoardCommentNotificationDto.of(notification)
        );
    }

    /**
     * 댓글 목록 조회
     *
     * @param boardId     - 게시판 아이디
     * @param boardMemoId - 게시글 아이디
     * @param pageable    - 페이지 및 정렬 조건
     * @return 댓글 정보 목록
     */
    public Page<BoardCommentDto> findBoardComments(
            Long boardId
            , Long boardMemoId
            , Pageable pageable
    ) {
        boardMemoService.getBoardMemo(boardId, boardMemoId);
        return boardCommentService.findBoardComments(boardMemoId, pageable);
    }

    /**
     * 댓글 수정
     *
     * @param boardId        - 게시판 아이디
     * @param boardMemoId    - 게시글 아이디
     * @param boardCommentId - 댓글 아이디
     * @param updateDto      - 수정 정보
     * @return 수정된 댓글 아이디
     */
    @Transactional
    public Long updateBoardComment(
            Long boardId
            , Long boardMemoId
            , Long boardCommentId
            , BoardCommentUpdateDto updateDto
    ) {

        //자원 계층 확인용
        boardMemoService.getBoardMemo(boardId, boardMemoId);
        BoardComment updateComment = boardCommentService.getBoardComment(boardMemoId, boardCommentId);
        ServiceUtil.validateOwner(updateComment.getUser().getUserId());
        updateComment.update(updateDto);

        return updateComment.getBoardCommentId();

    }

    /**
     * 댓글 삭제
     *
     * @param boardId        - 게시판 아이디
     * @param boardMemoId    - 게시글 아이디
     * @param boardCommentId - 댓글 아이디
     */
    @Transactional
    public void deleteBoardComment(Long boardId, Long boardMemoId, Long boardCommentId) {
        boardMemoService.getBoardMemo(boardId, boardMemoId);
        boardCommentService.deleteBoardComment(boardMemoId, boardCommentId);
    }

    /**
     * 댓글 알림 목록 조회
     */
    public List<BoardCommentNotificationDto> findBoardCommentNotifications() {
        return notificationService.findNotifications(CustomUserDetails.getDetails().getUserId());
    }

    /**
     * 댓글 알림 확인
     *
     * @param notiId - 알림 아이디
     */
    @Transactional
    public void checkBoardCommentNotification(Long notiId) {

        BoardCommentNotification notification = notificationService.getNotification(notiId);
        validateNotification(notification);
        notification.check();

    }

    private void validateNotification(BoardCommentNotification notification) {

        String userId = CustomUserDetails.getDetails().getUserId();
        if(ObjectUtils.isEmpty(notification.getComment().getParent())) {
            if(!notification.getComment().getBoardMemo().getUser().getUserId().equalsIgnoreCase(userId))
                throw new ResponseException("The notification not assigned to the current user", HttpStatus.BAD_REQUEST);

        } else {
            if(!notification.getComment().getParent().getUser().getUserId().equalsIgnoreCase(userId))
                throw new ResponseException("The notification not assigned to the current user", HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * 댓글 알림 삭제
     *
     * @param notiId - 알림 아이디
     */
    @Transactional
    public void deleteBoardCommentNotification(Long notiId) {

        BoardCommentNotification notification = notificationService.getNotification(notiId);
        validateNotification(notification);
        notificationService.deleteNotification(notiId);

    }

}
