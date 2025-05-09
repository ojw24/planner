package com.ojw.planner.app.community.board.service.comment;

import com.ojw.planner.app.community.board.domain.comment.BoardComment;
import com.ojw.planner.app.community.board.domain.dto.comment.BoardCommentDto;
import com.ojw.planner.app.community.board.repository.comment.BoardCommentRepository;
import com.ojw.planner.core.util.ServiceUtil;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardCommentService {

    private final BoardCommentRepository boardCommentRepository;

    public BoardComment saveBoardComment(BoardComment boardComment) {
        return boardCommentRepository.save(boardComment);
    }

    public Page<BoardCommentDto> findBoardComments(Long boardMemoId, Pageable pageable) {
        Page<BoardComment> findComments = getBoardComments(boardMemoId, pageable);
        return new PageImpl<>(
                findComments.getContent().stream().map(BoardCommentDto::of).collect(Collectors.toList())
                , findComments.getPageable()
                , findComments.getTotalElements()
        );
    }

    private Page<BoardComment> getBoardComments(Long boardMemoId, Pageable pageable) {
        return boardCommentRepository.findAll(boardMemoId, pageable);
    }

    public BoardComment getBoardComment(Long boardMemoId, Long boardCommentId) {
        return boardCommentRepository.find(boardMemoId, boardCommentId)
                .orElseThrow(() -> new ResponseException("not exist board comment : " + boardCommentId, HttpStatus.NOT_FOUND));
    }

    /**
     * 댓글 순서 조회
     *
     * @param boardMemoId    - 게시글 아이디
     * @param boardCommentId - 댓글 아이디
     * @return 댓글 순서
     */
    public Long findBoardCommentOrder(Long boardMemoId, Long boardCommentId) {
        
        BoardComment comment = getBoardComment(boardMemoId, boardCommentId);
        BoardComment root = comment.getRoot() != null ? comment.getRoot() : comment;
        return boardCommentRepository.getOrder(
                boardMemoId
                , root.getBoardCommentId()
                , root.getRegDtm()
        );
                
    }

    @Transactional
    public void deleteBoardComment(Long boardMemoId, Long boardCommentId) {
        BoardComment deleteComment = getBoardComment(boardMemoId, boardCommentId);
        ServiceUtil.validateOwner(deleteComment.getUser().getUserId());
        deleteComment.delete();
    }

}
