package com.ojw.planner.app.system.user.controller;

import com.ojw.planner.app.system.user.domain.dto.UserCreateDto;
import com.ojw.planner.app.system.user.domain.dto.UserFindDto;
import com.ojw.planner.app.system.user.domain.dto.UserUpdateDto;
import com.ojw.planner.app.system.user.domain.dto.redis.PwdResetRequest;
import com.ojw.planner.app.system.user.service.UserFacadeService;
import com.ojw.planner.app.system.user.service.UserService;
import com.ojw.planner.core.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 API")
@Validated
@RequestMapping("/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserFacadeService userFacadeService;

    private final UserService userService;

    @Operation(summary = "사용자 등록", tags = "User")
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateDto createDto) {
        return new ResponseEntity<>(new ApiResponse<>("User creation successful", userService.createUser(createDto)), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole(T(com.ojw.planner.core.enumeration.system.user.Authority).ADMIN.description)")
    @PageableAsQueryParam
    @Operation(summary = "사용자 목록 조회", description = "사용자 목록 조회(관리자)", tags = "User")
    @GetMapping(path = "/manage/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUsers(
            @ParameterObject @Valid UserFindDto findDto
            , @Parameter(hidden = true) @PageableDefault(sort = {"regDtm"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return new ResponseEntity<>(new ApiResponse<>(userService.findUsers(findDto, pageable)), HttpStatus.OK);
    }

    @Operation(summary = "사용자 목록 조회", tags = "User")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findSimpleUsers(@ParameterObject @Valid UserFindDto findDto) {
        return new ResponseEntity<>(new ApiResponse<>(userService.findSimpleUsers(findDto)), HttpStatus.OK);
    }

    @Operation(summary = "사용자 상세 조회", description = "UUID로 조회", tags = "User")
    @GetMapping(path = "/{uuid}")
    public ResponseEntity<?> findUser(
            @Parameter(name = "uuid", required = true) @NotBlank @PathVariable("uuid") String uuid
    ) {
        return new ResponseEntity<>(new ApiResponse<>(userService.findUser(uuid)), HttpStatus.OK);
    }

    @Operation(summary = "사용자 상세 조회", description = "사용자 직접 조회", tags = "User")
    @GetMapping(path = "/profile/me")
    public ResponseEntity<?> findMe() {
        return new ResponseEntity<>(new ApiResponse<>(userService.findUser()), HttpStatus.OK);
    }

    @Operation(summary = "아이디 중복 확인", tags = "User")
    @GetMapping(path = "/auth/duplicate-check")
    public ResponseEntity<?> duplicateCheck(
            @RequestParam(name = "userId") @NotBlank String userId
    ) {
        userService.validateUserId(userId);
        return new ResponseEntity<>(new ApiResponse<>("Duplicate check successful"), HttpStatus.OK);
    }

    @Operation(summary = "아이디 찾기", tags = "User")
    @GetMapping(path = "/auth/find-id")
    public ResponseEntity<?> findUserId(
            @Parameter(name = "email", required = true) @Email @NotBlank String email
    ) {
        userService.findUserId(email);
        return new ResponseEntity<>(new ApiResponse<>("find user id successful"), HttpStatus.OK);
    }

    @Operation(summary = "사용자 정보 수정", tags = "User")
    @PutMapping(path = "/{uuid}")
    public ResponseEntity<?> updateUser(
            @RequestBody @Valid UserUpdateDto updateDto
            , @Parameter(name = "uuid", required = true) @NotBlank @PathVariable("uuid") String uuid
    ) {
        userService.updateUser(uuid, updateDto);
        return new ResponseEntity<>(new ApiResponse<>("User update successful"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(T(com.ojw.planner.core.enumeration.system.user.Authority).ADMIN.description)")
    @Operation(summary = "사용자 정지", description = "사용자 아이디로 정지", tags = "User")
    @PutMapping(path = "/{uuid}/ban")
    public ResponseEntity<?> banUser(@Parameter(name = "uuid", required = true) @NotBlank @PathVariable("uuid") String uuid) {
        userService.banUser(uuid);
        return new ResponseEntity<>(new ApiResponse<>("User ban successful"), HttpStatus.OK);
    }

    @Operation(summary = "사용자 비밀번호 재설정", description = "비밀번호 재설정 메일 전송", tags = "User")
    @PutMapping(path = "/auth/find-password")
    public ResponseEntity<?> sendPasswordReset(
            @Parameter(name = "userId") String userId
            , @Parameter(name = "uuid") String uuid
    ) {
        userFacadeService.sendPasswordReset(userId, uuid);
        return new ResponseEntity<>(new ApiResponse<>("User password reset mail is sent"), HttpStatus.OK);
    }

    @Operation(summary = "사용자 비밀번호 재설정", description = "비밀번호 재설정", tags = "User")
    @PutMapping(path = "/auth/password")
    public ResponseEntity<?> userPasswordReset(
            @RequestBody @Valid PwdResetRequest request
    ) {
        userFacadeService.userPasswordReset(request);
        return new ResponseEntity<>(new ApiResponse<>("User password reset successful"), HttpStatus.OK);
    }

    @Operation(summary = "사용자 삭제", description = "사용자 아이디로 삭제", tags = "User")
    @DeleteMapping(path = "/{uuid}")
    public ResponseEntity<?> deleteUser(
            @Parameter(name = "uuid", required = true) @NotBlank @PathVariable("uuid") String uuid
    ) {
        userService.deleteUser(uuid);
        return new ResponseEntity<>(new ApiResponse<>("User delete successful"), HttpStatus.OK);
    }

    @Operation(summary = "사용자 알림 목록 조회", tags = "User")
    @GetMapping(path = "/notification/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUserNotifications() {
        return new ResponseEntity<>(new ApiResponse<>(userFacadeService.findNotifications()), HttpStatus.OK);
    }

}
