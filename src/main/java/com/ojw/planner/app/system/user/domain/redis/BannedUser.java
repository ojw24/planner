package com.ojw.planner.app.system.user.domain.redis;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@Builder
@RedisHash("bUser")
public class BannedUser {

    @Id
    private String userId;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expire;

}
