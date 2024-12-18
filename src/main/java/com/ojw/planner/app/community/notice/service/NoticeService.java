package com.ojw.planner.app.community.notice.service;

import com.ojw.planner.app.community.notice.domain.Notice;
import com.ojw.planner.app.community.notice.domain.dto.NoticeCreateDto;
import com.ojw.planner.app.community.notice.domain.dto.NoticeDto;
import com.ojw.planner.app.community.notice.domain.dto.NoticeFindDto;
import com.ojw.planner.app.community.notice.domain.dto.NoticeUpdateDto;
import com.ojw.planner.app.community.notice.repository.NoticeRepository;
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
public class NoticeService {

    private final NoticeRepository noticeRepository;

    /**
     * 공지사항 등록
     *
     * @param createDto - 등록 정보
     * @return 생성된 공지사항 아이디
     */
    @Transactional
    public Long createNotice(NoticeCreateDto createDto) {
        return noticeRepository.save(createDto.toEntity()).getNoticeId();
    }

    /**
     * 공지사항 목록 조회
     *
     * @param findDto  - 검색 정보
     * @param pageable - 페이지 및 정렬 조건
     * @return 공지사항 정보 목록
     */
    public Page<NoticeDto> findNotices(NoticeFindDto findDto, Pageable pageable) {
        Page<Notice> findNotices = getNotices(findDto, pageable);
        return new PageImpl<>(
                findNotices.getContent().stream().map(NoticeDto::of).collect(Collectors.toList())
                , findNotices.getPageable()
                , findNotices.getTotalElements()
        );
    }

    private Page<Notice> getNotices(NoticeFindDto findDto, Pageable pageable) {
        return noticeRepository.findAll(findDto, pageable);
    }

    /**
     * 공지사항 상세 조회
     *
     * @param noticeId - 공지사항
     * @return 공지사항 상세 정보
     */
    public NoticeDto findNotice(Long noticeId) {
        return NoticeDto.of(getNotice(noticeId));
    }

    private Notice getNotice(Long noticeId) {
        return noticeRepository.findByNoticeIdAndIsDeletedIsFalse(noticeId)
                .orElseThrow(() -> new ResponseException("not exist notice : " + noticeId, HttpStatus.NOT_FOUND));
    }

    /**
     * 공지사항 수정
     *
     * @param noticeId  - 공지사항 아이디
     * @param updateDto - 수정 정보
     * @return 수정된 공지사항 아이디
     */
    @Transactional
    public Long updateNotice(Long noticeId, NoticeUpdateDto updateDto) {
        getNotice(noticeId).update(updateDto);
        return noticeId;
    }

    /**
     * 공지사항 삭제
     *
     * @param noticeId - 공지사항 아이디
     */
    @Transactional
    public void deleteNotice(Long noticeId) {
        getNotice(noticeId).delete();
    }

}
