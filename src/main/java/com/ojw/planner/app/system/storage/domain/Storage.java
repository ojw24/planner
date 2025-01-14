package com.ojw.planner.app.system.storage.domain;

import com.ojw.planner.core.enumeration.system.storage.StorageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Builder
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "storage")
@Entity
public class Storage {

    @Comment("스토리지 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storage_id", nullable = false)
    private Long storageId;

    @Comment("스토리지 타입")
    @Column(name = "storage_type", nullable = false, length = 100)
    private StorageType storageType;

    @Comment("경로")
    @Column(name = "path", columnDefinition = "TEXT")
    private String path;

    @Comment("리전")
    @Column(name = "region")
    private String region;

    @Comment("아이디")
    @Column(name = "id")
    private String id;

    @Comment("비밀번호")
    @Column(name = "password")
    private String password;

}
