package com.ojw.planner.app.system.friend.domain.request;

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
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Builder
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "friend_req")
@Entity
public class FriendRequest {

    @Comment("친구 그룹 아이디")
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

    @Comment("생성일시")
    @Column(name = "req_dtm", nullable = false, updatable = false)
    private LocalDateTime reqDtm;

    @PrePersist
    public void onPrePersist() {
        this.reqDtm = Utils.now();
    }

}
