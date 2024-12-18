package com.ojw.planner.app.community.notice.domain.dto;

import com.ojw.planner.app.community.notice.domain.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDto {

    @Schema(description = "공지사항 아이디")
    private Long noticeId;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "내용")
    private String content;

    @Schema(description = "상단 고정 여부")
    private Boolean isTop;

    @Schema(description = "등록일시")
    private LocalDateTime regDtm;

    @Schema(description = "수정일시")
    private LocalDateTime updtDtm;

    public static NoticeDto of(Notice notice) {
        return NoticeDto.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .isTop(notice.getIsTop())
                .regDtm(notice.getRegDtm())
                .updtDtm(notice.getUpdtDtm())
                .build();
    }

}
