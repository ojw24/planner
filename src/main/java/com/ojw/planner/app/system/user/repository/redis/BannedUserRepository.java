package com.ojw.planner.app.system.user.repository.redis;

import com.ojw.planner.app.system.user.domain.redis.BannedUser;
import org.springframework.data.repository.CrudRepository;

public interface BannedUserRepository extends CrudRepository<BannedUser, String> {
}
