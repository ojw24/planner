package com.ojw.planner.app.system.user.domain.setting;

import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.app.system.user.domain.dto.UserUpdateDto.UserSettingUpdateDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Builder
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "users_setting")
@Entity
public class UserSetting {

    @Comment("설정 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id", nullable = false)
    private Long settingId;

    @Comment("사용자 아이디")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Comment("친구 신청 알림 여부")
    @Column(name = "is_friend_req_noti", nullable = false)
    @ColumnDefault("true")
    private Boolean isFriendReqNoti;

    @Comment("일정 공유 신청 알림 여부")
    @Column(name = "is_sch_share_req_noti", nullable = false)
    @ColumnDefault("true")
    private Boolean isSchShareReqNoti;

    @Comment("댓글 알림 여부")
    @Column(name = "is_comment_noti", nullable = false)
    @ColumnDefault("true")
    private Boolean isCommentNoti;

    public void update(UserSettingUpdateDto updateDto) {

        if(updateDto.getIsFriendReqNoti() != null)
            this.isFriendReqNoti = updateDto.getIsFriendReqNoti();

        if(updateDto.getIsSchShareReqNoti() != null)
            this.isSchShareReqNoti = updateDto.getIsSchShareReqNoti();

        if(updateDto.getIsCommentNoti() != null)
            this.isCommentNoti = updateDto.getIsCommentNoti();

    }

}
