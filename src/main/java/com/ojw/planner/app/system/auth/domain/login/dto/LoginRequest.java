package com.ojw.planner.app.system.auth.domain.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Schema(description = "사용자 아이디")
    @NotEmpty
    private String userId;

    @Schema(description = "비밀번호")
    @NotEmpty
    private String password;

}
