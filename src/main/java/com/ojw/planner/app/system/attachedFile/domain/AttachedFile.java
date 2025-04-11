package com.ojw.planner.app.system.attachedFile.domain;

import com.ojw.planner.app.system.storage.domain.Storage;
import com.ojw.planner.core.enumeration.system.attachedFile.Auth;
import com.ojw.planner.core.enumeration.system.attachedFile.FileType;
import com.ojw.planner.core.util.Utils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Builder
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "attc_file")
@Entity
public class AttachedFile {

    @Comment("첨부파일 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attc_file_id", nullable = false)
    private Long attcFileId;

    @Comment("스토리지 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_id", nullable = false)
    private Storage storage;

    @Comment("파일 타입")
    @Column(name = "file_type", nullable = false, length = 100)
    private FileType fileType;

    @Comment("권한")
    @Column(name = "auth", length = 100)
    private Auth auth;

    @Comment("원본 파일명")
    @Column(name = "name", nullable = false)
    private String name;

    @Comment("경로")
    @Column(name = "path", nullable = false, columnDefinition = "TEXT")
    private String path;

    @Comment("등록 일시")
    @Column(name = "reg_dtm", nullable = false, updatable = false)
    private LocalDateTime regDtm;

    @PrePersist
    public void onPrePersist() {
        this.regDtm = Utils.now();
    }

}
