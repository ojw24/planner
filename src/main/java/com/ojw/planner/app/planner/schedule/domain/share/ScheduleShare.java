package com.ojw.planner.app.planner.schedule.domain.share;

import com.ojw.planner.app.planner.schedule.domain.Schedule;
import com.ojw.planner.app.system.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Builder
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Getter
@IdClass(ScheduleShare.ScheduleSharePK.class)
@Table(name = "schedule_share")
@Entity
public class ScheduleShare {

    @Comment("일정 아이디")
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @Comment("사용자 아이디")
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleSharePK implements Serializable {

        private Long schedule;

        private String user;

    }

}
