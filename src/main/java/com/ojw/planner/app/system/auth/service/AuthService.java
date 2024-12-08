package com.ojw.planner.app.system.auth.service;

import com.ojw.planner.app.system.auth.domain.dto.RefreshDto;
import com.ojw.planner.app.system.auth.domain.log.dto.LoginRequest;
import com.ojw.planner.app.system.auth.domain.log.dto.LoginResponse;
import com.ojw.planner.app.system.auth.domain.token.Token;
import com.ojw.planner.app.system.auth.service.token.TokenService;
import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.app.system.user.service.UserService;
import com.ojw.planner.core.enumeration.inner.JwtClaim;
import com.ojw.planner.core.enumeration.inner.JwtType;
import com.ojw.planner.core.util.JwtUtil;
import com.ojw.planner.core.util.Utils;
import com.ojw.planner.exception.ResponseException;
import io.jsonwebtoken.ExpiredJwtException;
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

    private final JwtUtil jwtUtil;

    /**
     * 로그인
     *
     * @param request - 로그인 요청
     * @return 로그인 응답
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {

        User user = userService.getUser(request.getUserId());
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new ResponseException("Password is wrong", HttpStatus.UNAUTHORIZED);

        if(user.getIsBanned()) throw new ResponseException("banned user : " + request.getUserId(), HttpStatus.FORBIDDEN);

        String accessToken = jwtUtil.createToken(user, JwtType.ACCESS);
        String refreshToken = jwtUtil.createToken(user, JwtType.REFRESH);

        tokenService.createToken(
                Token.builder()
                        .user(user)
                        .refreshToken(refreshToken)
                        .expiredDtm(
                                jwtUtil.getSubject(refreshToken, JwtType.REFRESH)
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

    /**
     * 로그아웃
     *
     * @param jwt        - jwt(access token)
     * @param refreshDto - 리프레쉬 dto
     */
    @Transactional
    public void logout(String jwt, RefreshDto refreshDto) {

        //TODO : access token from jwt -> save cache logic

        expire(refreshDto);

    }

    public void expire(RefreshDto refreshDto) {

        String refreshToken = refreshDto.getRefreshToken();
        Token token = tokenService.getTokenByRefresh(refreshToken);

        checkRefreshToken(refreshToken, token);
        token.expire();

    }

    private void checkRefreshToken(String jwt, Token token) {

        //DB 사전체크
        if(token.getExpiredDtm().isBefore(Utils.now()))
            throw new ResponseException("Refresh token is expired. Please login again.", HttpStatus.FORBIDDEN);

        try {
            if(!jwtUtil.validateToken(jwt, JwtType.REFRESH))
                throw new ResponseException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            throw new ResponseException("Refresh token is expired. Please login again.", HttpStatus.FORBIDDEN);
        }

    }

    /**
     * 토큰 리프레쉬
     *
     * @param refreshDto - 리프레쉬 dto
     * @return 갱신 토큰 정보
     */
    public LoginResponse refresh(RefreshDto refreshDto) {

        String refreshToken = refreshDto.getRefreshToken();
        User user = userService.getUser(
                jwtUtil.getSubject(refreshToken, JwtType.REFRESH)
                        .get(JwtClaim.ID.getType(), JwtClaim.ID.getType().getClass())
        );

        checkRefreshToken(refreshToken, tokenService.getTokenByRefresh(refreshToken));

        return LoginResponse.builder()
                .userId(user.getUserId())
                .accessToken(jwtUtil.createToken(user, JwtType.ACCESS))
                .refreshToken(refreshToken)
                .build();

    }

}
