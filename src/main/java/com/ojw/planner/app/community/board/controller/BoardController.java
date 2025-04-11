package com.ojw.planner.app.community.board.controller;

import com.ojw.planner.app.community.board.domain.dto.comment.BoardCommentCreateDto;
import com.ojw.planner.app.community.board.domain.dto.comment.BoardCommentUpdateDto;
import com.ojw.planner.app.community.board.domain.dto.memo.BoardMemoCreateDto;
import com.ojw.planner.app.community.board.domain.dto.memo.BoardMemoFindDto;
import com.ojw.planner.app.community.board.domain.dto.memo.BoardMemoUpdateDto;
import com.ojw.planner.app.community.board.service.BoardFacadeService;
import com.ojw.planner.app.community.board.service.comment.BoardCommentService;
import com.ojw.planner.app.community.board.service.memo.BoardMemoService;
import com.ojw.planner.core.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Board", description = "게시판 API")
@Validated
@RequestMapping("/board")
@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardFacadeService boardFacadeService;

    private final BoardMemoService boardMemoService;

    private final BoardCommentService boardCommentService;

    @Operation(summary = "게시글 등록", tags = "Board")
    @PostMapping(path = "/{boardId}/memo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createBoardMemo(
            @RequestBody @Valid BoardMemoCreateDto createDto
            , @Parameter(name = "boardId", required = true) @NotNull @Positive
            @PathVariable("boardId") Long boardId
    ) {
        return new ResponseEntity<>(
                new ApiResponse<>("Board memo creation successful", boardFacadeService.createBoardMemo(boardId, createDto))
                , HttpStatus.CREATED
        );
    }

    @PageableAsQueryParam
    @Operation(summary = "게시글 목록 조회", tags = "Board")
    @GetMapping(path = "/{boardId}/memo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findBoardMemos(
            @ParameterObject @Valid BoardMemoFindDto findDto
            , @Parameter(name = "boardId", required = true) @NotNull @Positive
            @PathVariable("boardId") Long boardId
            , @Parameter(hidden = true) @PageableDefault(sort = {"regDtm"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return new ResponseEntity<>(new ApiResponse<>(boardMemoService.findBoardMemos(boardId, findDto, pageable)), HttpStatus.OK);
    }

    @Operation(summary = "게시글 상세 조회", tags = "Board")
    @GetMapping(path = "/{boardId}/memo/{boardMemoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findBoardMemo(
            @Parameter(name = "boardId", required = true) @NotNull @Positive
            @PathVariable("boardId") Long boardId
            , @Parameter(name = "boardMemoId", required = true) @NotNull @Positive
            @PathVariable("boardMemoId") Long boardMemoId
    ) {
        return new ResponseEntity<>(new ApiResponse<>(boardFacadeService.findBoardMemo(boardId, boardMemoId)), HttpStatus.OK);
    }

    @Operation(summary = "게시글 수정", tags = "Board")
    @PutMapping(path = "/{boardId}/memo/{boardMemoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBoardMemo(
            @Parameter(name = "boardId", required = true) @NotNull @Positive
            @PathVariable("boardId") Long boardId
            , @Parameter(name = "boardMemoId", required = true) @NotNull @Positive
            @PathVariable("boardMemoId") Long boardMemoId
            , @RequestBody @Valid BoardMemoUpdateDto updateDto
    ) {
        boardFacadeService.updateBoardMemo(boardId, boardMemoId, updateDto);
        return new ResponseEntity<>(new ApiResponse<>("Board memo update successful"), HttpStatus.OK);
    }

    @Operation(summary = "게시글 삭제", tags = "Board")
    @DeleteMapping(path = "/{boardId}/memo/{boardMemoId}")
    public ResponseEntity<?> deleteBoardMemo(
            @Parameter(name = "boardId", required = true) @NotNull @Positive
            @PathVariable("boardId") Long boardId
            , @Parameter(name = "boardMemoId", required = true) @NotNull @Positive
            @PathVariable("boardMemoId") Long boardMemoId
    ) {
        boardFacadeService.deleteBoardMemo(boardId, boardMemoId);
        return new ResponseEntity<>(new ApiResponse<>("Board memo delete successful"), HttpStatus.OK);
    }

    @Operation(summary = "댓글 등록", tags = "Board")
    @PostMapping(path = "/{boardId}/memo/{boardMemoId}/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createBoardComment(
            @RequestBody @Valid BoardCommentCreateDto createDto
            , @Parameter(name = "boardId", required = true) @NotNull @Positive
            @PathVariable("boardId") Long boardId
            , @Parameter(name = "boardMemoId", required = true) @NotNull @Positive
            @PathVariable("boardMemoId") Long boardMemoId
    ) {
        return new ResponseEntity<>(
                new ApiResponse<>("Board comment creation successful", boardFacadeService.createBoardComment(boardId, boardMemoId, createDto))
                , HttpStatus.CREATED
        );
    }

    @PageableAsQueryParam
    @Operation(summary = "댓글 목록 조회", tags = "Board")
    @GetMapping(path = "/{boardId}/memo/{boardMemoId}/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findBoardComments(
            @Parameter(name = "boardId", required = true) @NotNull @Positive
            @PathVariable("boardId") Long boardId
            , @Parameter(name = "boardMemoId", required = true) @NotNull @Positive
            @PathVariable("boardMemoId") Long boardMemoId
            , @Parameter(hidden = true) @PageableDefault(sort = {"regDtm", "boardCommentId"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return new ResponseEntity<>(new ApiResponse<>(boardFacadeService.findBoardComments(boardId, boardMemoId, pageable)), HttpStatus.OK);
    }

    @Operation(summary = "댓글 순서 조회", tags = "Board")
    @GetMapping(path = "/memo/{boardMemoId}/comment/{boardCommentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findBoardCommentOrder(
            @Parameter(name = "boardMemoId", required = true) @NotNull @Positive
            @PathVariable("boardMemoId") Long boardMemoId
            , @Parameter(name = "boardCommentId", required = true) @NotNull @Positive
            @PathVariable("boardCommentId") Long boardCommentId
    ) {
        return new ResponseEntity<>(new ApiResponse<>(boardCommentService.findBoardCommentOrder(boardMemoId, boardCommentId)), HttpStatus.OK);
    }

    @Operation(summary = "댓글 수정", tags = "Board")
    @PutMapping(path = "/{boardId}/memo/{boardMemoId}/comment/{boardCommentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBoardComment(
            @Parameter(name = "boardId", required = true) @NotNull @Positive
            @PathVariable("boardId") Long boardId
            , @Parameter(name = "boardMemoId", required = true) @NotNull @Positive
            @PathVariable("boardMemoId") Long boardMemoId
            , @Parameter(name = "boardCommentId", required = true) @NotNull @Positive
            @PathVariable("boardCommentId") Long boardCommentId
            , @RequestBody @Valid BoardCommentUpdateDto updateDto
    ) {
        boardFacadeService.updateBoardComment(boardId, boardMemoId, boardCommentId, updateDto);
        return new ResponseEntity<>(new ApiResponse<>("Board comment update successful"), HttpStatus.OK);
    }

    @Operation(summary = "댓글 삭제", tags = "Board")
    @DeleteMapping(path = "/{boardId}/memo/{boardMemoId}/comment/{boardCommentId}")
    public ResponseEntity<?> deleteBoardComment(
            @Parameter(name = "boardId", required = true) @NotNull @Positive
            @PathVariable("boardId") Long boardId
            , @Parameter(name = "boardMemoId", required = true) @NotNull @Positive
            @PathVariable("boardMemoId") Long boardMemoId
            , @Parameter(name = "boardCommentId", required = true) @NotNull @Positive
            @PathVariable("boardCommentId") Long boardCommentId
    ) {
        boardFacadeService.deleteBoardComment(boardId, boardMemoId, boardCommentId);
        return new ResponseEntity<>(new ApiResponse<>("Board comment delete successful"), HttpStatus.OK);
    }

    @Operation(summary = "댓글 알림 목록 조회", tags = "Board")
    @GetMapping(path = "/memo/comment/notification", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findBoardCommentNotifications() {
        return new ResponseEntity<>(new ApiResponse<>(boardFacadeService.findBoardCommentNotifications()), HttpStatus.OK);
    }

    @Operation(summary = "댓글 알림 확인", tags = "Board")
    @PutMapping(path = "/memo/comment/notification/{notiId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkBoardCommentNotification(
            @Parameter(name = "notiId", required = true) @NotNull @Positive
            @PathVariable("notiId") Long notiId
    ) {
        boardFacadeService.checkBoardCommentNotification(notiId);
        return new ResponseEntity<>(new ApiResponse<>("Board comment notification check successful"), HttpStatus.OK);
    }

    @Operation(summary = "댓글 알림 삭제", tags = "Board")
    @DeleteMapping(path = "/memo/comment/notification/{notiId}")
    public ResponseEntity<?> deleteBoardCommentNotification(
            @Parameter(name = "notiId", required = true) @NotNull @Positive
            @PathVariable("notiId") Long notiId
    ) {
        boardFacadeService.deleteBoardCommentNotification(notiId);
        return new ResponseEntity<>(new ApiResponse<>("Board comment notification delete successful"), HttpStatus.OK);
    }

    @Operation(summary = "댓글 MQ 바인딩", tags = "Board")
    @PostMapping(path = "/mq", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createBoardMQ(@RequestParam(name = "uuid") @NotBlank String uuid) {
        boardFacadeService.declareBinding(uuid);
        return new ResponseEntity<>(
                new ApiResponse<>("Board mq creation successful")
                , HttpStatus.CREATED
        );
    }

}
