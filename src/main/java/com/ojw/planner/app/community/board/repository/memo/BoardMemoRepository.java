package com.ojw.planner.app.community.board.repository.memo;

import com.ojw.planner.app.community.board.domain.memo.BoardMemo;
import com.ojw.planner.app.community.board.repository.memo.querydsl.BoardMemoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardMemoRepository
        extends JpaRepository<BoardMemo, Long>, BoardMemoRepositoryCustom {

    @Modifying
    @Query("UPDATE BoardMemo m SET m.hit = :hit WHERE m.boardMemoId = :id")
    void updateHit(Long id, Long hit);

}
