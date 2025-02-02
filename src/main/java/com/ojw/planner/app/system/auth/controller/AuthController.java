package com.ojw.planner.app.system.auth.controller;

import com.ojw.planner.app.system.auth.domain.dto.RefreshDto;
import com.ojw.planner.app.system.auth.domain.log.dto.LoginRequest;
import com.ojw.planner.app.system.auth.domain.log.dto.LoginResponse;
import com.ojw.planner.app.system.auth.service.AuthService;
import com.ojw.planner.core.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "인증 API")
@Validated
@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인", tags = "Auth")
    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authService.generateRefreshCookie(response.getRefreshToken()))
                .body(response);
    }

    @Operation(summary = "로그아웃", tags = "Auth")
    @DeleteMapping(path = "/logout")
    public ResponseEntity<?> logout(
            @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt
            , @RequestBody @Valid RefreshDto refreshDto
    ) {
        authService.logout(jwt, refreshDto);
        return new ResponseEntity<>(new ApiResponse<>("success"),HttpStatus.OK);
    }

    @Operation(summary = "토큰 갱신", tags = "Auth")
    @PostMapping(path = "/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        return new ResponseEntity<>(new ApiResponse<>(authService.refresh(request)), HttpStatus.OK);
    }

}
