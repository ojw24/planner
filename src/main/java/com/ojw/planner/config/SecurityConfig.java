package com.ojw.planner.config;

import com.ojw.planner.app.system.auth.service.token.BannedTokenService;
import com.ojw.planner.app.system.user.service.redis.BannedUserService;
import com.ojw.planner.app.system.user.service.security.CustomUserDetailsService;
import com.ojw.planner.core.util.JwtUtil;
import com.ojw.planner.filter.CommonFilter;
import com.ojw.planner.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final BannedTokenService bannedTokenService;

    private final BannedUserService bannedUserService;

    private final CustomUserDetailsService customUserDetailsService;

    private final JwtUtil jwtUtil;

    //비밀번호 암호화를 위해 PasswordEncoder를 BCryptPasswordEncoder로 bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/swagger-ui/**"
                , "/v3/api-docs/**"
                , "/swagger-resources/**"
                , "/webjars/**"
                , "/error"
                , "/auth/login"
                , "/auth/refresh"
                , "/user/auth/**"
                , "/actuator/health"
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((a) -> a.anyRequest().permitAll())
                .addFilterBefore(new CommonFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(
                        new JwtFilter(
                                bannedTokenService
                                , bannedUserService
                                , customUserDetailsService
                                , jwtUtil
                        ), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
