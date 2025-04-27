package com.ojw.planner.app.community.notice.controller;

import com.ojw.planner.app.community.notice.domain.dto.NoticeCreateDto;
import com.ojw.planner.app.community.notice.domain.dto.NoticeFindDto;
import com.ojw.planner.app.community.notice.domain.dto.NoticeUpdateDto;
import com.ojw.planner.app.community.notice.service.NoticeService;
import com.ojw.planner.core.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Notice", description = "공지사항 API")
@Validated
@RequestMapping("/notice")
@RequiredArgsConstructor
@RestController
public class NoticeController {

    private final NoticeService noticeService;

    @PreAuthorize("hasRole(T(com.ojw.planner.core.enumeration.system.user.Authority).ADMIN.description)")
    @Operation(summary = "공지사항 등록", tags = "Notice")
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNotice(@RequestBody @Valid NoticeCreateDto createDto) {
        return new ResponseEntity<>(new ApiResponse<>("Notice creation successful", noticeService.createNotice(createDto)), HttpStatus.CREATED);
    }

    @PageableAsQueryParam
    @Operation(summary = "공지사항 목록 조회", tags = "Notice")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findNotices(
            @ParameterObject @Valid NoticeFindDto findDto
            , @Parameter(hidden = true) @PageableDefault(sort = {"regDtm"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return new ResponseEntity<>(new ApiResponse<>(noticeService.findNotices(findDto, pageable)), HttpStatus.OK);
    }

    @Operation(summary = "공지사항 상세 조회", tags = "Notice")
    @GetMapping(path = "/{noticeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findNotice(
            @Parameter(name = "noticeId", required = true) @NotNull @Positive @PathVariable("noticeId") Long noticeId
    ) {
        return new ResponseEntity<>(new ApiResponse<>(noticeService.findNotice(noticeId)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(T(com.ojw.planner.core.enumeration.system.user.Authority).ADMIN.description)")
    @Operation(summary = "공지사항 수정", tags = "Notice")
    @PutMapping(path = "/{noticeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateNotice(
            @RequestBody @Valid NoticeUpdateDto updateDto
            , @Parameter(name = "noticeId", required = true) @NotNull @Positive @PathVariable("noticeId") Long noticeId
    ) {
        noticeService.updateNotice(noticeId, updateDto);
        return new ResponseEntity<>(new ApiResponse<>("Notice update successful"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(T(com.ojw.planner.core.enumeration.system.user.Authority).ADMIN.description)")
    @Operation(summary = "공지사항 삭제", tags = "Notice")
    @DeleteMapping(path = "/{noticeId}")
    public ResponseEntity<?> deleteNotice(
            @Parameter(name = "noticeId", required = true) @NotNull @Positive @PathVariable("noticeId") Long noticeId
    ) {
        noticeService.deleteNotice(noticeId);
        return new ResponseEntity<>(new ApiResponse<>("Notice delete successful"), HttpStatus.OK);
    }

}
