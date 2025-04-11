package com.ojw.planner.app.community.board.service;

import com.ojw.planner.app.community.board.domain.Board;
import com.ojw.planner.app.community.board.repository.BoardRepository;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public Board getBoard(Long boardId) {
        return boardRepository.findByBoardIdAndIsDeletedIsFalse(boardId)
                .orElseThrow(() -> new ResponseException("not exist board : " + boardId, HttpStatus.NOT_FOUND));
    }

}
