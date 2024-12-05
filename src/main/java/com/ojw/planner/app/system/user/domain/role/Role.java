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
import org.springframework.security.core.GrantedAuthority;

@Builder
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "role")
@Entity
public class Role implements GrantedAuthority {

    @Comment("권한 아이디")
    @Id
    @Column(name = "role_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Comment("권한")
    @Column(name = "authority", nullable = false)
    private Authority authority;

    @Override
    public String getAuthority() {
        return authority.getDescription();
    }

}
