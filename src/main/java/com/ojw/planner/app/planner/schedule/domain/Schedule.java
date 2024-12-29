package com.ojw.planner.app.planner.schedule.domain;

import com.ojw.planner.app.planner.goal.domain.Goal;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleUpdateDto;
import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@SuperBuilder
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "schedule")
@Entity
public class Schedule extends BaseEntity {

    @Comment("일정 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id", nullable = false)
    private Long scheduleId;

    @Comment("사용자 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Comment("목표 아이디")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", updatable = false)
    private Goal goal;

    @Comment("일정명")
    @Column(name = "name", nullable = false)
    private String name;

    @Comment("일정 시작일시")
    @Column(name = "start_dtm", nullable = false)
    private LocalDateTime startDtm;

    @Comment("일정 종료일시")
    @Column(name = "end_dtm", nullable = false)
    private LocalDateTime endDtm;

    @Comment("일정명")
    @Column(name = "location")
    private String location;

    public void update(ScheduleUpdateDto updateDto) {

        if(StringUtils.hasText(updateDto.getName())) this.name = updateDto.getName();

        if(!ObjectUtils.isEmpty(updateDto.getStartDtm())) this.startDtm = updateDto.getStartDtm();

        if(!ObjectUtils.isEmpty(updateDto.getEndDtm())) this.endDtm = updateDto.getEndDtm();

        if(StringUtils.hasText(updateDto.getLocation())) this.location = updateDto.getLocation();

    }

    public void delete() {
        this.isDeleted = true;
    }

}
