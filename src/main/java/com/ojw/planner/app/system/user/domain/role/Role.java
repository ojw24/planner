package com.ojw.planner.app.system.user.domain.role;

import com.ojw.planner.core.enumeration.system.user.Authority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Builder
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "role")
@Entity
public class Role {

    @Comment("권한 아이디")
    @Id
    @Column(name = "role_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Comment("권한")
    @Column(name = "authority", nullable = false)
    private Authority authority;

}
