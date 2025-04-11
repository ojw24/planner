package com.ojw.planner.app.system.user.domain.security;

import com.ojw.planner.app.system.user.domain.role.Role;
import com.ojw.planner.core.enumeration.system.user.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomAuthority implements GrantedAuthority {

    private Authority authority;

    public static CustomAuthority of(Role role) {
        return CustomAuthority.builder()
                .authority(role.getAuthority())
                .build();
    }

    @Override
    public String getAuthority() {
        return this.authority.getDescription();
    }

}
