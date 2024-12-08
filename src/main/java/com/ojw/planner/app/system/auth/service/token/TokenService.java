package com.ojw.planner.app.system.auth.service.token;

import com.ojw.planner.app.system.auth.domain.token.Token;
import com.ojw.planner.app.system.auth.repository.TokenRepository;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    public Token getTokenByRefresh(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ResponseException("not exist token", HttpStatus.NOT_FOUND));
    }

}
