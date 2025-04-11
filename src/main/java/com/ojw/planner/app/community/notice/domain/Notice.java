package com.ojw.planner.app.community.notice.domain;

import com.ojw.planner.app.community.notice.domain.dto.NoticeUpdateDto;
import com.ojw.planner.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@SuperBuilder
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "notice")
@Entity
public class Notice extends BaseEntity {

    @Comment("공지사항 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id", nullable = false)
    private Long noticeId;

    @Comment("제목")
    @Column(name = "title", nullable = false)
    private String title;

    @Comment("내용")
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Comment("상단 고정 여부")
    @Column(name = "is_top", nullable = false)
    @ColumnDefault("false")
    private Boolean isTop;

    public void update(NoticeUpdateDto updateDto) {

        if(StringUtils.hasText(updateDto.getTitle())) this.title = updateDto.getTitle();

        if(StringUtils.hasText(updateDto.getContent())) this.content = updateDto.getContent();

        if(!ObjectUtils.isEmpty(updateDto.getIsTop())) this.isTop = updateDto.getIsTop();

    }

    public void delete() {
        this.isDeleted = true;
    }

}
