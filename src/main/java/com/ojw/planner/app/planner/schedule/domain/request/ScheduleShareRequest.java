package com.ojw.planner.app.planner.schedule.domain.request;

import com.ojw.planner.app.planner.schedule.domain.Schedule;
import com.ojw.planner.app.system.user.domain.User;
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
@Table(name = "schedule_share_req")
@Entity
public class ScheduleShareRequest {

    @Comment("신청 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "req_id", nullable = false)
    private Long reqId;

    @Comment("일정 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false, updatable = false)
    private Schedule schedule;

    @Comment("요청자 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false, updatable = false)
    private User requester;

    @Comment("대상자 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false, updatable = false)
    private User target;

    @Comment("신청일시")
    @Column(name = "req_dtm", nullable = false, updatable = false)
    private LocalDateTime reqDtm;

    @PrePersist
    public void onPrePersist() {
        this.reqDtm = Utils.now();
    }

}
