package com.ojw.planner.app.community.board.repository.comment.querydsl;

import com.ojw.planner.app.community.board.domain.comment.BoardComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BoardCommentRepositoryCustom {

    Page<BoardComment> findAll(Long boardMemoId, Pageable pageable);

    Optional<BoardComment> find(Long boardMemoId, Long boardCommentId);

}
