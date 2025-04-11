package com.ojw.planner.app.system.auth.domain.redis.token;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@Builder
@RedisHash("bToken")
public class BannedToken {

    @Id
    private String token;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expire;

}
