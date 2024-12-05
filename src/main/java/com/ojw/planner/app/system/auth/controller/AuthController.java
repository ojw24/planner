package com.ojw.planner.app.system.auth.controller;

import com.ojw.planner.app.system.auth.domain.login.dto.LoginRequest;
import com.ojw.planner.app.system.auth.service.AuthService;
import com.ojw.planner.core.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return new ResponseEntity<>(new ApiResponse<>(authService.login(request)), HttpStatus.OK);
    }

}
