package com.ojw.planner.app.system.auth.service.token;

import com.ojw.planner.app.system.auth.domain.token.Token;
import com.ojw.planner.app.system.auth.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    @Transactional
    public Long createToken(Token token) {
        return tokenRepository.save(token).getTokenId();
    }

}
