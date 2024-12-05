package com.ojw.planner.app.system.user.service;

import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.app.system.user.domain.dto.UserCreateDTO;
import com.ojw.planner.app.system.user.domain.dto.UserDTO;
import com.ojw.planner.app.system.user.domain.dto.UserFindDTO;
import com.ojw.planner.app.system.user.domain.dto.UserUpdateDTO;
import com.ojw.planner.app.system.user.domain.role.UserRole;
import com.ojw.planner.app.system.user.repository.UserRepository;
import com.ojw.planner.app.system.user.repository.role.RoleRepository;
import com.ojw.planner.app.system.user.repository.role.UserRoleRepository;
import com.ojw.planner.core.enumeration.system.user.Authority;
import com.ojw.planner.core.util.SMTPUtil;
import com.ojw.planner.core.util.dto.smtp.SMTPRequest;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final SMTPUtil smtpUtil;

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final RoleRepository roleRepository;

    /**
     * 사용자 등록
     *
     * @param createDTO - 등록 정보
     * @return 생성된 사용자 아이디
     */
    @Transactional
    public String createUser(UserCreateDTO createDTO) {

        validateUserId(createDTO.getUserId());

        User createUser = userRepository.save(createDTO.toEntity(passwordEncoder.encode(createDTO.getPassword())));
        createLinked(createUser);

        return createUser.getUserId();

    }

    public void validateUserId(String userId) {
        if(!ObjectUtils.isEmpty(userRepository.findByUserIdAndIsDeletedIsFalse(userId)))
            throw new ResponseException("already exist user ID : " + userId, HttpStatus.CONFLICT);
    }

    private void createLinked(User user) {
        userRoleRepository.save(
                UserRole.builder()
                        .user(user)
                        .role(
                                roleRepository.findByAuthority(Authority.USER)
                                        .orElseThrow(() -> new RuntimeException("Internal Server Error"))
                        )
                        .build()
        );
    }

    /**
     * 사용자 목록 조회
     *
     * @param findDto  - 검색 정보
     * @param pageable - 페이지 및 정렬 조건
     * @return 사용자 정보 목록
     */
    public Page<UserDTO> findUsers(UserFindDTO findDto, Pageable pageable) {
        Page<User> findUsers = getUsers(findDto, pageable);
        return new PageImpl<>(
                findUsers.getContent().stream().map(UserDTO::of).collect(Collectors.toList())
                , findUsers.getPageable()
                , findUsers.getTotalElements()
        );
    }

    private Page<User> getUsers(UserFindDTO findDto, Pageable pageable) {
        return userRepository.findAll(findDto, pageable);
    }

    /**
     * 사용자 상세 조회
     *
     * @param userId - 사용자 관리 아이디
     * @return 사용자 상세 정보
     */
    public UserDTO findUser(String userId){
        return UserDTO.of(getUser(userId));
    }

    public User getUser(String userId) {
        return userRepository.findByUserIdAndIsDeletedIsFalse(userId)
                .orElseThrow(() -> new ResponseException("not exist user : " + userId, HttpStatus.NOT_FOUND));
    }

    /**
     * 아이디 찾기
     *
     * @param email - 사용자 이메일
     * @return 사용자 아이디
     */
    public String findUserId(String email){
        return userRepository.findByEmailAndIsDeletedIsFalse(email)
                .orElseThrow(() -> new ResponseException("not exist user : " + email, HttpStatus.NOT_FOUND))
                .getUserId();
    }

    /**
     * 사용자 수정
     *
     * @param userId    - 사용자 아이디
     * @param updateDto - 사용자 수정 정보
     * @return 수정된 사용자 아이디
     */
    @Transactional
    public String updateUser(String userId, UserUpdateDTO updateDto) {

        if(StringUtils.hasText(updateDto.getPassword()))
            updateDto.setPassword(passwordEncoder.encode(updateDto.getPassword()));

        getUser(userId).update(updateDto);
        return userId;

    }

    /**
     * 사용자 정지
     *
     * @param userId - 사용자 아이디
     */
    @Transactional
    public void banUser(String userId) {
        getUser(userId).ban();
    }

    /**
     * 비밀번호 재설정
     *
     * @param userId - 사용자 아이디
     */
    @Transactional
    public void userPasswordReset(String userId) {
        smtpUtil.send(
                SMTPRequest.builder()
                    .to(getUser(userId).getEmail())
                    .subject("[Planner] 비밀번호 재설정")
                    .build()
                    .passwordReset(userId)
        );
    }

    /**
     * 사용자 삭제
     *
     * @param userId - 사용자 아이디
     */
    @Transactional
    public void deleteUser(String userId) {
        getUser(userId).delete();
    }

}
