package com.ojw.planner.app.system.auth.service;

import com.ojw.planner.app.system.auth.domain.log.dto.LoginRequest;
import com.ojw.planner.app.system.auth.domain.log.dto.LoginResponse;
import com.ojw.planner.app.system.auth.domain.redis.token.BannedToken;
import com.ojw.planner.app.system.auth.domain.redis.token.RToken;
import com.ojw.planner.app.system.auth.service.token.BannedTokenService;
import com.ojw.planner.app.system.auth.service.token.TokenService;
import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.app.system.user.service.UserService;
import com.ojw.planner.core.enumeration.inner.JwtClaim;
import com.ojw.planner.core.enumeration.inner.JwtType;
import com.ojw.planner.core.util.JwtUtil;
import com.ojw.planner.core.util.Utils;
import com.ojw.planner.exception.ResponseException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final TokenService tokenService;

    private final UserService userService;

    private final BannedTokenService bannedTokenService;

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

        User user = userService.getUser(request.getUserId(), true);
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new ResponseException("아이디 혹은 비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED);

        if(user.getIsBanned()) throw new ResponseException("정지된 유저 : " + request.getUserId(), HttpStatus.FORBIDDEN);

        String accessToken = jwtUtil.createToken(user, JwtType.ACCESS);
        String refreshToken = jwtUtil.createToken(user, JwtType.REFRESH);

        tokenService.saveToken(
                RToken.builder()
                        .refreshToken(refreshToken)
                        .expire(Utils.getExpire(jwtUtil.getSubject(refreshToken, JwtType.REFRESH).getExpiration()))
                        .build()
                        .setDefaultValues()
        );

        return LoginResponse.builder()
                .userId(user.getUserId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    public String generateRefreshCookie(String refreshToken) {
        return ResponseCookie.from(JwtType.REFRESH.name(), refreshToken)
                .httpOnly(true)
                //.secure(true) //TODO : 서버 배포 후 도메인 및 ssl 설정 시 해제
                .path("/")
                .maxAge(JwtType.REFRESH.getExpire() / 1000)
                .sameSite(org.springframework.boot.web.server.Cookie.SameSite.STRICT.attributeValue())
                .build()
                .toString();
    }

    /**
     * 로그아웃
     *
     * @param jwt     - jwt(access token)
     * @param request - http 요청
     */
    @Transactional
    public void logout(String jwt, HttpServletRequest request) {

        jwt = JwtUtil.removeType(jwt);
        bannedTokenService.saveToken(
                BannedToken.builder()
                        .token(jwt)
                        .expire(Utils.getExpire(jwtUtil.getSubject(jwt, JwtType.ACCESS).getExpiration()))
                        .build()
        );

        expire(getRefreshToken(request));

    }

    public void expire(String refreshToken) {

        if(StringUtils.hasText(refreshToken)) {

            checkRefreshToken(refreshToken);

            RToken token = tokenService.getTokenByRefresh(refreshToken);
            token.expire();
            tokenService.saveToken(token);

        }

    }

    private void checkRefreshToken(String jwt) {

        try {
            if(!jwtUtil.validateToken(jwt, JwtType.REFRESH))
                throw new ResponseException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            throw new ResponseException("세션이 만료되었습니다. 다시 로그인 해주세요.", HttpStatus.FORBIDDEN);
        }

    }

    /**
     * 토큰 리프레쉬
     *
     * @param request - http 요청
     * @return 갱신 토큰 정보
     */
    public LoginResponse refresh(HttpServletRequest request) {

        String refreshToken = getRefreshToken(request);
        if(!StringUtils.hasText(refreshToken))
            throw new ResponseException("not exist token in request", HttpStatus.UNAUTHORIZED);

        checkRefreshToken(refreshToken);
        User user = userService.getUser(
                jwtUtil.getSubject(refreshToken, JwtType.REFRESH)
                        .get(JwtClaim.ID.getType(), JwtClaim.ID.getType().getClass())
        );

        tokenService.getTokenByRefresh(refreshToken);

        return LoginResponse.builder()
                .userId(user.getUserId())
                .accessToken(jwtUtil.createToken(user, JwtType.ACCESS))
                .build();

    }

    private String getRefreshToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        String refreshToken = null;

        if (cookies != null) {

            for (Cookie cookie : cookies) {

                if (JwtType.REFRESH.name().equalsIgnoreCase(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }

            }

        }

        return refreshToken;

    }

}
