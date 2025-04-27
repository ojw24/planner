package com.ojw.planner.app.system.user.domain.security;

import com.ojw.planner.app.system.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private String userId;

    private String uuid;

    private String password;

    private String username;

    private List<CustomAuthority> authorities;

    public static CustomUserDetails of(User user) {
        return CustomUserDetails.builder()
                .userId(user.getUserId())
                .uuid(user.getUuid())
                .password(user.getPassword())
                .username(user.getPassword())
                .authorities(
                        ObjectUtils.isEmpty(user.getRoles())
                                ? null
                                : user.getRoles().stream()
                                .map(r -> CustomAuthority.of(r.getRole()))
                                .collect(Collectors.toList())
                )
                .build();
    }

    public UsernamePasswordAuthenticationToken getAuth() {
        return new UsernamePasswordAuthenticationToken(this, null, this.authorities);
    }

    public static CustomUserDetails getDetails() {
        return (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
