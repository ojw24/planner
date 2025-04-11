package com.ojw.planner.app.community.board.repository.memo.querydsl;

import com.ojw.planner.app.community.board.domain.dto.memo.BoardMemoFindDto;
import com.ojw.planner.app.community.board.domain.memo.BoardMemo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BoardMemoRepositoryCustom {

    Page<BoardMemo> findAll(Long boardId, BoardMemoFindDto findDto, Pageable pageable);

    Optional<BoardMemo> find(Long boardId, Long boardMemoId);

}
