package com.ojw.planner.app.system.friend.domain.request;

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

@SuperBuilder
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "friend_req")
@Entity
public class FriendRequest extends BaseEntity {

    @Comment("친구 신청 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_req_id", nullable = false)
    private Long friendReqId;

    @Comment("요청자 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Comment("대상자 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private User target;

    public void delete() {
        this.isDeleted = true;
    }

}
