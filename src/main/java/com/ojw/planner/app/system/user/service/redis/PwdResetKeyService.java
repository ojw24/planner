package com.ojw.planner.app.system.user.service.redis;

import com.ojw.planner.app.system.user.domain.redis.PwdResetKey;
import com.ojw.planner.app.system.user.repository.redis.PwdResetKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PwdResetKeyService {

    private final PwdResetKeyRepository pwdResetKeyRepository;

    public String saveKey(PwdResetKey key) {
        return pwdResetKeyRepository.save(key).getKey();
    }

    public PwdResetKey getKey(String key) {
        return pwdResetKeyRepository.findById(key).orElse(null);
    }

}
