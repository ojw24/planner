package com.ojw.planner.app.system.auth.domain.dto;

import com.ojw.planner.core.util.JwtUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "리프레쉬 Dto")
public class RefreshDto {

    @Schema(description = "리프레쉬 토큰")
    @NotEmpty
    private String refreshToken;

    public String getRefreshToken() {
        this.refreshToken = JwtUtil.removeType(this.refreshToken);
        return this.refreshToken;
    }

}
