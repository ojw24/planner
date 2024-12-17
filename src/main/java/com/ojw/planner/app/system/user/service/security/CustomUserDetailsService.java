package com.ojw.planner.app.system.user.service.security;

import com.ojw.planner.app.system.user.domain.security.CustomUserDetails;
import com.ojw.planner.app.system.user.repository.UserRepository;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 사용자 인증 객체 얻기
     *
     * @param userId - 사용자 아이디
     * @return 사용자 인증 객체
     */
    public UsernamePasswordAuthenticationToken getUserAuth(String userId) {
        return loadUserByUsername(userId).getAuth();
    }

    @Override
    public CustomUserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return CustomUserDetails.of(
                userRepository.findByUserIdAndIsDeletedIsFalse(userId)
                        .orElseThrow(() -> new ResponseException("not exist user : " + userId, HttpStatus.NOT_FOUND))
        );
    }

}
