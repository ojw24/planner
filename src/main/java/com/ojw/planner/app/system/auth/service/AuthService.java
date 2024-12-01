package com.ojw.planner.app.system.auth.service;

import com.ojw.planner.app.system.auth.domain.login.dto.LoginRequest;
import com.ojw.planner.app.system.auth.domain.login.dto.LoginResponse;
import com.ojw.planner.app.system.auth.domain.token.Token;
import com.ojw.planner.app.system.auth.service.token.TokenService;
import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.app.system.user.service.UserService;
import com.ojw.planner.core.enumeration.inner.JWTType;
import com.ojw.planner.core.util.JWTUtil;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final TokenService tokenService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final JWTUtil jwtUtil;

    @Transactional
    public LoginResponse login(LoginRequest request) {

        User user = userService.getUser(request.getUserId());
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new ResponseException("Password is wrong", HttpStatus.UNAUTHORIZED);

        String accessToken = jwtUtil.createToken(user, JWTType.ACCESS);
        String refreshToken = jwtUtil.createToken(user, JWTType.REFRESH);

        tokenService.createToken(
                Token.builder()
                        .user(user)
                        .refreshToken(refreshToken)
                        .expiredDtm(
                                jwtUtil.getSubject(refreshToken, JWTType.REFRESH)
                                .getExpiration().toInstant()
                                .atZone(ZoneId.systemDefault())  // 시스템 기본 시간대
                                .toLocalDateTime()
                        )
                        .build()
        );

        return LoginResponse.builder()
                .userId(user.getUserId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

}
