package com.ojw.planner.app.system.friend.domain.group;

import com.ojw.planner.app.system.friend.domain.Friend;
import com.ojw.planner.app.system.friend.domain.dto.group.FriendGroupUpdateDto;
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
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Builder
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "friend_grp")
@Entity
public class FriendGroup {

    @Comment("친구 그룹 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_grp_id", nullable = false)
    private Long friendGrpId;

    @Comment("사용자 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Comment("그룹명")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Comment("순서")
    @Column(name = "ord", nullable = false)
    private Double ord;

    @OneToMany(mappedBy = "friendGroup")
    private List<Friend> friends = new ArrayList<>();

    public void update(FriendGroupUpdateDto updateDto) {

        if(StringUtils.hasText(updateDto.getName())) this.name = updateDto.getName();

        if(!ObjectUtils.isEmpty(updateDto.getOrd())) this.ord = updateDto.getOrd();

    }

}
