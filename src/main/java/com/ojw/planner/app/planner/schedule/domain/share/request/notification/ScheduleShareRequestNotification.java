package com.ojw.planner.app.planner.schedule.domain.share.request.notification;

import com.ojw.planner.app.planner.schedule.domain.share.request.ScheduleShareRequest;
import com.ojw.planner.core.enumeration.common.NotificationType;
import com.ojw.planner.core.util.Utils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
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
@Table(name = "schedule_share_req_noti")
@Entity
public class ScheduleShareRequestNotification {

    @Comment("알림 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noti_id", nullable = false)
    private Long notiId;

    @Comment("신청 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "req_id", nullable = false)
    private ScheduleShareRequest request;

    @Comment("알림 타입")
    @Column(name = "noti_type", nullable = false, length = 100)
    private NotificationType notiType;

    @Comment("확인 여부")
    @ColumnDefault("false")
    @Column(name = "is_checked", nullable = false)
    private Boolean isChecked;

    @Comment("생성 일시")
    @Column(name = "reg_dtm", nullable = false, updatable = false)
    private LocalDateTime regDtm;

    @PrePersist
    public void onPrePersist() {
        this.regDtm = Utils.now();
    }

    public void check() {
        this.isChecked = true;
    }

}
