package com.ojw.planner.app.system.auth.repository.redis;

import com.ojw.planner.app.system.auth.domain.redis.token.BannedToken;
import org.springframework.data.repository.CrudRepository;

public interface BannedTokenRepository extends CrudRepository<BannedToken, String> {
}
