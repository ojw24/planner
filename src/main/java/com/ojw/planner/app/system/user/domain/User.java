package com.ojw.planner.app.system.user.domain;

import com.ojw.planner.app.system.attachedFile.domain.AttachedFile;
import com.ojw.planner.app.system.user.domain.dto.UserUpdateDto;
import com.ojw.planner.app.system.user.domain.role.UserRole;
import com.ojw.planner.app.system.user.domain.setting.UserSetting;
import com.ojw.planner.core.domain.BaseEntity;
import com.ojw.planner.core.enumeration.system.user.Authority;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuperBuilder
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "users")
@Entity
public class User extends BaseEntity {

    @Comment("사용자 아이디")
    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private String userId;

    @Comment("고유 키")
    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    private String uuid;

    @Comment("비밀번호")
    @Column(name = "password", nullable = false)
    private String password;

    @Comment("이름")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Comment("이메일")
    @Column(name = "email", nullable = false)
    private String email;

    @Comment("정지 여부")
    @Column(name = "is_banned", nullable = false)
    @ColumnDefault("false")
    private Boolean isBanned;

    @OneToMany(mappedBy = "user")
    private List<UserRole> roles = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private UserSetting setting;

    @Comment("첨부파일 아이디")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attc_file_id")
    private AttachedFile attachedFile;

    @Override
    public void onPrePersist() {
        super.onPrePersist();
        this.uuid = UUID.randomUUID().toString();
    }

    public void update(UserUpdateDto updateDto, AttachedFile attachedFile) {

        if(StringUtils.hasText(updateDto.getPassword())) this.password = updateDto.getPassword();

        if(StringUtils.hasText(updateDto.getName())) this.name = updateDto.getName();

        if(StringUtils.hasText(updateDto.getEmail())) this.email = updateDto.getEmail();

        this.attachedFile = attachedFile;

    }

    public void delete() {
        this.isDeleted = true;
    }

    public void ban() {
        this.isBanned = !this.isBanned;
    }

    public boolean isAdmin() {
        return !ObjectUtils.isEmpty(this.getRoles())
                && this.getRoles().stream().anyMatch(r -> r.getRole().getAuthority().equals(Authority.ADMIN));
    }

}
