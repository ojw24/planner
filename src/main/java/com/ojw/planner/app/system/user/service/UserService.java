package com.ojw.planner.app.system.user.service;

import com.ojw.planner.app.system.attachedFile.domain.AttachedFile;
import com.ojw.planner.app.system.attachedFile.repository.AttachedFileRepository;
import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.app.system.user.domain.dto.UserCreateDto;
import com.ojw.planner.app.system.user.domain.dto.UserDto;
import com.ojw.planner.app.system.user.domain.dto.UserDto.UserSimpleDto;
import com.ojw.planner.app.system.user.domain.dto.UserFindDto;
import com.ojw.planner.app.system.user.domain.dto.UserUpdateDto;
import com.ojw.planner.app.system.user.domain.dto.UserUpdateDto.UserSettingUpdateDto;
import com.ojw.planner.app.system.user.domain.dto.redis.PwdResetRequest;
import com.ojw.planner.app.system.user.domain.redis.BannedUser;
import com.ojw.planner.app.system.user.domain.role.UserRole;
import com.ojw.planner.app.system.user.domain.security.CustomUserDetails;
import com.ojw.planner.app.system.user.domain.setting.UserSetting;
import com.ojw.planner.app.system.user.repository.UserRepository;
import com.ojw.planner.app.system.user.repository.role.RoleRepository;
import com.ojw.planner.app.system.user.repository.role.UserRoleRepository;
import com.ojw.planner.app.system.user.repository.setting.UserSettingRepository;
import com.ojw.planner.app.system.user.service.redis.BannedUserService;
import com.ojw.planner.core.enumeration.inner.JwtType;
import com.ojw.planner.core.enumeration.system.user.Authority;
import com.ojw.planner.core.util.SMTPUtil;
import com.ojw.planner.core.util.ServiceUtil;
import com.ojw.planner.core.util.dto.smtp.SMTPRequest;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    @Value("${spring.mail.path.home}")
    private String homePath;

    @Value("${spring.mail.path.password}")
    private String passwordPath;

    private final BannedUserService bannedUserService;

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final UserSettingRepository userSettingRepository;

    private final RoleRepository roleRepository;

    private final AttachedFileRepository attachedFileRepository;

    private final PasswordEncoder passwordEncoder;

    private final SMTPUtil smtpUtil;

    /**
     * 사용자 등록
     *
     * @param createDto - 등록 정보
     * @return 생성된 사용자 아이디
     */
    @Transactional
    public String createUser(UserCreateDto createDto) {

        validateCreateDto(createDto);

        User createUser = userRepository.save(createDto.toEntity(passwordEncoder.encode(createDto.getPassword())));
        createLinked(createUser);

        return createUser.getUserId();

    }

    public void validateCreateDto(UserCreateDto createDto) {

        validateUserId(createDto.getUserId());

        if(userRepository.findByEmailAndIsDeletedIsFalse(createDto.getEmail()).isPresent())
            throw new ResponseException("해당 이메일로 가입된 아이디가 있습니다.", HttpStatus.CONFLICT);

    }

    public void validateUserId(String userId) {
        if(userRepository.findByUserIdAndIsDeletedIsFalse(userId).isPresent())
            throw new ResponseException("이미 존재하는 ID입니다 : " + userId, HttpStatus.CONFLICT);
    }

    private void createLinked(User user) {
        createRole(user);
        createSetting(user);
    }

    private void createRole(User user) {
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

    private void createSetting(User user) {
        userSettingRepository.save(
                UserSetting.builder()
                        .user(user)
                        .build()
        );
    }

    /**
     * 사용자 목록 조회(관리자)
     *
     * @param findDto  - 검색 정보
     * @param pageable - 페이지 및 정렬 조건
     * @return 사용자 정보 목록
     */
    public Page<UserDto> findUsers(UserFindDto findDto, Pageable pageable) {
        Page<User> findUsers = getUsers(findDto, pageable);
        return new PageImpl<>(
                findUsers.getContent().stream().map(UserDto::of).collect(Collectors.toList())
                , findUsers.getPageable()
                , findUsers.getTotalElements()
        );
    }

    private Page<User> getUsers(UserFindDto findDto, Pageable pageable) {
        return userRepository.findAll(findDto, pageable);
    }

    /**
     * 사용자 목록 조회
     *
     * @param findDto - 검색 정보
     * @return 사용자 정보 목록
     */
    public List<UserSimpleDto> findSimpleUsers(UserFindDto findDto) {
        return userRepository.findSimples(findDto, CustomUserDetails.getDetails().getUserId());
    }

    /**
     * 사용자 상세 조회
     *
     * @param userId - 사용자 아이디
     * @return 사용자 상세 정보
     */
    public UserDto findUser(String userId){
        return UserDto.of(getUser(userId), true);
    }

    /**
     * 사용자 상세 조회
     *
     * @return 사용자 상세 정보
     */
    public UserDto findUser(){
        return UserDto.of(getUser(CustomUserDetails.getDetails().getUserId()), true);
    }

    public User getUser(String userId) {
        return getUser(userId, false);
    }

    public User getUser(String userId, boolean login) {
        return userRepository.findByUserIdAndIsDeletedIsFalse(userId)
                .orElseThrow(() -> new ResponseException(
                        login ? "아이디 혹은 비밀번호가 올바르지 않습니다." : "존재하지 않는 사용자입니다 : " + userId
                        , HttpStatus.NOT_FOUND
                ));
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmailAndIsDeletedIsFalse(email)
                .orElseThrow(() -> new ResponseException("해당 이메일로 가입된 사용자가 없습니다.", HttpStatus.NOT_FOUND));
    }

    /**
     * 아이디 찾기
     *
     * @param email - 사용자 이메일
     */
    public void findUserId(String email) {
        smtpUtil.send(
                SMTPRequest.builder()
                        .to(email)
                        .subject("[Planner] 아이디 안내")
                        .build()
                        .findId(getUserByEmail(email).getUserId(), homePath)
        );
    }

    @Transactional
    public String updateUser(String userId, UserUpdateDto updateDto) {
        return updateUser(userId, updateDto, true);
    }

    /**
     * 사용자 수정
     *
     * @param userId    - 사용자 아이디
     * @param updateDto - 수정 정보
     * @return 수정된 사용자 아이디
     */
    @Transactional
    public String updateUser(String userId, UserUpdateDto updateDto, boolean update) {

        validateUpdateDto(userId, updateDto, update);

        User updateUser = getUser(userId);
        updateUser.update(
                updateDto
                , ObjectUtils.isEmpty(updateDto.getAttcFileId())
                        ? null
                        : attachedFileRepository.findById(updateDto.getAttcFileId())
                            .orElse(null)
        );
        updateLinked(updateUser, updateDto);

        return userId;

    }

    private void validateUpdateDto(String userId, UserUpdateDto updateDto, boolean update) {

        if(update) ServiceUtil.validateOwner(userId);

        if(StringUtils.hasText(updateDto.getPassword()))
            updateDto.setPassword(passwordEncoder.encode(updateDto.getPassword()));

        if(StringUtils.hasText(updateDto.getEmail())) {
            if(userRepository.findByEmailAndUserIdNotAndIsDeletedIsFalse(updateDto.getEmail(), userId).isPresent())
                throw new ResponseException("해당 이메일로 가입된 아이디가 있습니다.", HttpStatus.CONFLICT);
        }

    }

    private void updateLinked(User user, UserUpdateDto updateDto) {
        updateSetting(user, updateDto.getSettingUpdateDto());
    }

    private void updateSetting(User user, UserSettingUpdateDto updateDto) {
        if(!ObjectUtils.isEmpty(updateDto)) user.getSetting().update(updateDto);
    }

    /**
     * 사용자 정지
     *
     * @param userId - 사용자 아이디
     */
    @Transactional
    public void banUser(String userId) {
        getUser(userId).ban();
        bannedUserService.saveUser(
                BannedUser.builder()
                        .userId(userId)
                        .expire(JwtType.ACCESS.getExpire())
                        .build()
        );
    }

    public void sendPasswordReset(String userId, String key) {
        smtpUtil.send(
                SMTPRequest.builder()
                    .to(getUser(userId).getEmail())
                    .subject("[Planner] 비밀번호 재설정")
                    .build()
                    .passwordReset(userId, passwordPath, key)
        );
    }

    @Transactional
    public void userPasswordReset(PwdResetRequest request) {

        AttachedFile file = getUser(request.getUserId()).getAttachedFile();
        UserUpdateDto pwdResetDto = UserUpdateDto.builder()
                .password(request.getPassword())
                .attcFileId(file != null ? file.getAttcFileId() : null)
                .build();

        updateUser(request.getUserId(), pwdResetDto, false);

    }

    /**
     * 사용자 삭제
     *
     * @param userId - 사용자 아이디
     */
    @Transactional
    public void deleteUser(String userId) {
        ServiceUtil.validateOwner(userId);
        getUser(userId).delete();
    }

}
