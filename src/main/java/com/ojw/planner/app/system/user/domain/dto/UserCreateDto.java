package com.ojw.planner.app.system.user.domain.dto;

import com.ojw.planner.app.system.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 등록 DTO")
public class UserCreateDto {

    @NotBlank
    @Schema(description = "사용자 아이디")
    private String userId;

    @NotBlank
    @Schema(description = "비밀번호")
    private String password;

    @NotBlank
    @Schema(description = "이름")
    private String name;

    @Email
    @NotBlank
    @Schema(description = "이메일")
    private String email;

    public User toEntity(String password) {
        return User.builder()
                .userId(this.userId)
                .password(password)
                .name(this.name)
                .email(this.email)
                .build();
    }

}
