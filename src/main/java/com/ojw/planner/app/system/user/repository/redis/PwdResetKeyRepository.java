package com.ojw.planner.app.system.user.repository.redis;

import com.ojw.planner.app.system.user.domain.redis.PwdResetKey;
import org.springframework.data.repository.CrudRepository;

public interface PwdResetKeyRepository extends CrudRepository<PwdResetKey, String> {
}
