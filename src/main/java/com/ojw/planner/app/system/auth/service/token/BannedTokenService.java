package com.ojw.planner.app.system.auth.service.token;

import com.ojw.planner.app.system.auth.domain.redis.token.BannedToken;
import com.ojw.planner.app.system.auth.repository.redis.BannedTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
@Service
public class BannedTokenService {

    private final BannedTokenRepository bannedTokenRepository;

    public String saveToken(BannedToken token) {
        return bannedTokenRepository.save(token).getToken();
    }

    public boolean existToken(String token) {
        return !ObjectUtils.isEmpty(bannedTokenRepository.findById(token).orElse(null));
    }

}
