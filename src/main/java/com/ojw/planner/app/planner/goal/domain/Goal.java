package com.ojw.planner.app.planner.goal.domain;

import com.ojw.planner.app.planner.goal.domain.dto.GoalUpdateDto;
import com.ojw.planner.app.planner.schedule.domain.Schedule;
import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.core.domain.BaseEntity;
import com.ojw.planner.core.enumeration.planner.goal.GoalType;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "goal")
@Entity
public class Goal extends BaseEntity {

    @Comment("목표 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id", nullable = false)
    private Long goalId;

    @Comment("사용자 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Comment("상위 목표 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_goal_id", updatable = false)
    private Goal parent;

    @Comment("목표명")
    @Column(name = "name", nullable = false)
    private String name;

    @Comment("달성 여부")
    @Column(name = "is_achieve", nullable = false)
    @ColumnDefault("false")
    private Boolean isAchieve;

    @Comment("목표 타입")
    @Column(name = "goal_type", nullable = false, updatable = false)
    private GoalType goalType;

    @Comment("목표 시작일")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Comment("목표 종료일")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "parent")
    private List<Goal> children = new ArrayList<>();

    @OneToOne(mappedBy = "goal", fetch = FetchType.LAZY)
    private Schedule schedule;

    public void update(GoalUpdateDto updateDto) {

        if(StringUtils.hasText(updateDto.getName())) this.name = updateDto.getName();

        if(updateDto.getIsArchive() != null) this.isAchieve = updateDto.getIsArchive();

        if(!ObjectUtils.isEmpty(updateDto.getStartDate())) this.startDate = updateDto.getStartDate();

        if(!ObjectUtils.isEmpty(updateDto.getEndDate())) this.endDate = updateDto.getEndDate();

    }

    public void delete() {
        this.isDeleted = true;
    }

}
