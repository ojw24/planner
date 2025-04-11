package com.ojw.planner.app.community.board.service.memo;

import com.ojw.planner.app.community.board.domain.dto.memo.BoardMemoDto;
import com.ojw.planner.app.community.board.domain.dto.memo.BoardMemoFindDto;
import com.ojw.planner.app.community.board.domain.memo.BoardMemo;
import com.ojw.planner.app.community.board.repository.memo.BoardMemoRepository;
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
public class BoardMemoService {

    private final BoardMemoRepository boardMemoRepository;

    public BoardMemo saveBoardMemo(BoardMemo boardMemo) {
        return boardMemoRepository.save(boardMemo);
    }

    /**
     * 게시글 목록 조회
     *
     * @param findDto  - 검색 정보
     * @param pageable - 페이지 및 정렬 조건
     * @return 게시글 정보 목록
     */
    public Page<BoardMemoDto> findBoardMemos(Long boardId, BoardMemoFindDto findDto, Pageable pageable) {
        Page<BoardMemo> findMemos = getBoardMemos(boardId, findDto, pageable);
        return new PageImpl<>(
                findMemos.getContent().stream().map(BoardMemoDto::of).collect(Collectors.toList())
                , findMemos.getPageable()
                , findMemos.getTotalElements()
        );
    }

    private Page<BoardMemo> getBoardMemos(Long boardId, BoardMemoFindDto findDto, Pageable pageable) {
        return boardMemoRepository.findAll(boardId, findDto, pageable);
    }

    public BoardMemo getBoardMemo(Long boardId, Long boardMemoId) {
        return boardMemoRepository.find(boardId, boardMemoId)
                .orElseThrow(() -> new ResponseException("not exist board memo : " + boardMemoId, HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void deleteBoardMemo(Long boardId, Long boardMemoId) {
        BoardMemo deleteMemo = getBoardMemo(boardId, boardMemoId);
        ServiceUtil.validateOwner(deleteMemo.getUser().getUserId());
        deleteMemo.delete();
    }

}
