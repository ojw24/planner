package com.ojw.planner.app.system.auth.service.token;

import com.ojw.planner.app.system.auth.domain.redis.token.RToken;
import com.ojw.planner.app.system.auth.repository.redis.RTokenRepository;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final RTokenRepository rTokenRepository;

    public String saveToken(RToken token) {
        return rTokenRepository.save(token).getRefreshToken();
    }

    public RToken getTokenByRefresh(String refreshToken) {
        return rTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new ResponseException("not exist token", HttpStatus.NOT_FOUND));
    }

}
