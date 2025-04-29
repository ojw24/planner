package com.ojw.planner.app.system.user.service.redis;

import com.ojw.planner.app.system.user.domain.redis.BannedUser;
import com.ojw.planner.app.system.user.repository.redis.BannedUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
@Service
public class BannedUserService {

    private final BannedUserRepository bannedUserRepository;

    public String saveUser(BannedUser user) {
        return bannedUserRepository.save(user).getUserId();
    }

    public boolean existUser(String userId) {
        return !ObjectUtils.isEmpty(bannedUserRepository.findById(userId).orElse(null));
    }

    public void deleteUser(BannedUser user) {
        bannedUserRepository.delete(user);
    }

}
