package com.ojw.planner.app.system.auth.repository.redis;

import com.ojw.planner.app.system.auth.domain.redis.token.RToken;
import org.springframework.data.repository.CrudRepository;

public interface RTokenRepository extends CrudRepository<RToken, String> {
}
