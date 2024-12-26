package com.ojw.planner.app.community.board.repository;

import com.ojw.planner.app.community.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByBoardIdAndIsDeletedIsFalse(Long boardId);

}
