package com.ojw.planner.app.system.friend.domain;

import com.ojw.planner.app.system.friend.domain.dto.FriendUpdateDto;
import com.ojw.planner.app.system.friend.domain.group.FriendGroup;
import com.ojw.planner.app.system.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.ObjectUtils;

@Builder
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "friend")
@Entity
public class Friend {

    @Comment("친구 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id", nullable = false)
    private Long friendId;

    @Comment("친구 그룹 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_grp_id")
    private FriendGroup friendGroup;

    @Comment("사용자 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Comment("친구 사용자 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_user_id", nullable = false)
    private User friend;

    @Comment("순서")
    @Column(name = "ord", nullable = false)
    private Double ord;

    public void update(FriendUpdateDto updateDto, FriendGroup friendGroup) {

        if(!ObjectUtils.isEmpty(updateDto.getOrd())) this.ord = updateDto.getOrd();

        this.friendGroup = friendGroup;

    }

}
