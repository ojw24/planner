package com.ojw.planner.app.system.auth.domain.token;

import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.core.util.Utils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "token")
@Entity
public class Token {

    @Comment("토큰 아이디")
    @Id
    @Column(name = "token_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @Comment("사용자 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;

    @Comment("리프레시토큰")
    @Column(name = "refresh_token", nullable = false, columnDefinition = "TEXT")
    private String refreshToken;

    @Comment("만료일시")
    @Column(name = "expired_dtm", nullable = false)
    private LocalDateTime expiredDtm;

    @Comment("로그인 일시")
    @Column(name = "login_dtm", nullable = false)
    private LocalDateTime loginDtm;

    @Comment("로그인 IP")
    @Column(name = "login_ip", nullable = false, length = 39)
    private String loginIp;

    @PrePersist
    private void setDefaultValues() {
        this.loginDtm = Utils.now();
        this.loginIp = Utils.getUserIp();
    }

    public void expire() {
        this.expiredDtm = Utils.now();
    }

}
