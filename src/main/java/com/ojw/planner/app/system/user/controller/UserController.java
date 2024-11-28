package com.ojw.planner.app.system.user.controller;

import com.ojw.planner.app.system.user.domain.dto.UserCreateDTO;
import com.ojw.planner.app.system.user.domain.dto.UserFindDTO;
import com.ojw.planner.app.system.user.domain.dto.UserUpdateDTO;
import com.ojw.planner.app.system.user.service.UserService;
import com.ojw.planner.core.response.APIResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 API")
@Validated
@RequestMapping("/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @Operation(summary = "사용자 등록", tags = "User")
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateDTO createDTO) {
        return new ResponseEntity<>(new APIResponse<>("User creation successful", userService.createUser(createDTO)), HttpStatus.CREATED);
    }

    @PageableAsQueryParam
    @Operation(summary = "사용자 목록 조회", description = "사용자 목록 조회", tags = "User")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUsers(
            @ParameterObject @Valid UserFindDTO userFindDto
            , @Parameter(hidden = true) @PageableDefault(sort = {"regDtm"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return new ResponseEntity<>(new APIResponse<>(userService.findUsers(userFindDto, pageable)), HttpStatus.OK);
    }

    @Operation(summary = "사용자 상세 조회", description = "사용자 상세 조회", tags = "User")
    @GetMapping(path = "/{userId}")
    public ResponseEntity<?> findUser(
            @Parameter(name = "userId", required = true) @NotBlank @PathVariable("userId") String userId
    ) {
        return new ResponseEntity<>(new APIResponse<>(userService.findUser(userId)), HttpStatus.OK);
    }

    @Operation(summary = "사용자 정보 수정", description = "사용자 정보 수정", tags = "User")
    @PutMapping(path = "/{userId}")
    public ResponseEntity<?> updateUser(
            @RequestBody @Valid UserUpdateDTO updateDto
            , @Parameter(name = "userId", required = true) @NotBlank @PathVariable("userId") String userId
    ) {
        userService.updateUser(userId, updateDto);
        return new ResponseEntity<>(new APIResponse<>("User update successful"), HttpStatus.OK);
    }

    @Operation(summary = "사용자 삭제", description = "사용자 아이디로 삭제", tags = "User")
    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<?> deleteUser(
            @Parameter(name = "userId", required = true) @NotBlank @PathVariable("userId") String userId
    ) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(new APIResponse<>("User delete successful"), HttpStatus.OK);
    }

}
