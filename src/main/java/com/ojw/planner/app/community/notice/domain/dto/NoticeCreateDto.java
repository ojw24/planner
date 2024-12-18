package com.ojw.planner.app.community.notice.domain.dto;

import com.ojw.planner.app.community.notice.domain.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "공지사항 등록 DTO")
public class NoticeCreateDto {

    @NotBlank
    @Schema(description = "제목")
    private String title;

    @NotBlank
    @Schema(description = "내용")
    private String content;
    
    @Schema(description = "상단 고정 여부")
    private Boolean isTop;

    public Notice toEntity() {
        return Notice.builder()
                .title(this.title)
                .content(this.content)
                .isTop(this.isTop)
                .build();
    }

}
