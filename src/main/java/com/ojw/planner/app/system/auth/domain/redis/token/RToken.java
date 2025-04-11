package com.ojw.planner.app.system.auth.domain.redis.token;

import com.ojw.planner.core.util.Utils;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Getter
@Builder
@RedisHash("token")
public class RToken {

    //리프레쉬 토큰
    @Id
    private String refreshToken;

    //사용자 아이디
    private String userId;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expire;

    //로그인 시간
    private LocalDateTime loginDtm;

    //로그인 IP
    private String loginIp;

    public RToken setDefaultValues() {
        this.loginDtm = Utils.now();
        this.loginIp = Utils.getUserIp();
        return this;
    }

    public void expire() {
        this.expire = 1000l;
    }

}
