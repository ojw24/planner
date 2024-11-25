package com.ojw.planner.core.domain;

import com.ojw.planner.core.util.Utils;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {

    @Comment("생성일시")
    @CreatedDate
    @Column(name = "reg_dtm", updatable = false, nullable = false)
    private LocalDateTime regDtm;

    @Comment("수정일시")
    @LastModifiedDate
    @Column(name = "updt_dtm")
    private LocalDateTime updtDtm;

    @Comment("삭제 여부")
    @Column(name = "is_deleted", nullable = false)
    protected Boolean isDeleted;

    @PrePersist
    public void onPrePersist() {
        this.regDtm = Utils.now();
        this.isDeleted = false;
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updtDtm = Utils.now();
    }

}
