package com.ojw.planner.app.system.user.domain.dto.redis;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "비밀번호 재설정 요청")
public class PwdResetRequest {

    @NotBlank
    @Schema(description = "사용자 아이디")
    private String userId;

    @NotBlank
    @Schema(description = "재설정 키")
    private String key;

    @NotBlank
    @Schema(description = "비밀번호")
    private String password;

}
