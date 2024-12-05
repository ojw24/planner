package com.ojw.planner.app.system.user.domain.role;

import com.ojw.planner.app.system.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@IdClass(UserRole.UserRolePK.class)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "users_role")
public class UserRole {

    @Comment("사용자 아이디")
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Comment("권한 아이디")
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId")
    private Role role;

    @Getter
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRolePK implements Serializable {

        private String user;

        private Long role;

    }

}
