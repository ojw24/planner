package com.ojw.planner.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ojw.planner.app.system.auth.service.token.BannedTokenService;
import com.ojw.planner.app.system.user.service.BannedUserService;
import com.ojw.planner.core.enumeration.inner.JwtClaim;
import com.ojw.planner.core.enumeration.inner.JwtPrefix;
import com.ojw.planner.core.enumeration.inner.JwtType;
import com.ojw.planner.core.response.ApiResponse;
import com.ojw.planner.core.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final BannedTokenService bannedTokenService;

    private final BannedUserService bannedUserService;

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        //사용자 등록 통과
        if(request.getMethod().equalsIgnoreCase("POST")
                && request.getRequestURI().contains("/user")) {
            chain.doFilter(request, response);
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.hasText(jwt)) {
            if (!checkToken(response, jwt)) return;
        } else {
            setResponse(response, "No token");
            return;
        }

        chain.doFilter(request, response);

    }

    private boolean checkToken(HttpServletResponse response, String jwt) throws IOException {

        if(jwt.contains(JwtPrefix.PREFIX.getType())) {
            jwt = jwt.replaceAll(JwtPrefix.PREFIX.getType(), "");
        } else {
            setResponse(response, "Invalid token type");
            return false;
        }

        try {
            if(!jwtUtil.validateToken(jwt, JwtType.ACCESS)) {
                setResponse(response, "Invalid token");
                return false;
            }
        } catch (ExpiredJwtException e) {
            setResponse(response, HttpStatus.FORBIDDEN, "Access token is expired. Please refresh.");
            return false;
        }

        if(
            bannedTokenService.existToken(jwt)
                || bannedUserService.existUser(
                        jwtUtil.getSubject(jwt, JwtType.ACCESS)
                                .get(JwtClaim.ID.getType(), JwtClaim.ID.getType().getClass())
                    )
        ) {
            setResponse(response, "User logged out or banned");
            return false;
        }

        return true;

    }

    private static void setResponse(HttpServletResponse response, String msg) throws IOException {
        setResponse(response, HttpStatus.UNAUTHORIZED, msg);
    }

    private static void setResponse(HttpServletResponse response, HttpStatus status, String msg) throws IOException {
        response.setStatus(status.value());
        try(PrintWriter writer = response.getWriter()) {
            writer.write(new ObjectMapper().writeValueAsString(new ApiResponse<>(false, msg)));
        }

    }

}
