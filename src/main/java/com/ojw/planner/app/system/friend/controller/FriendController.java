package com.ojw.planner.app.system.friend.controller;

import com.ojw.planner.app.system.friend.domain.dto.FriendUpdateDto;
import com.ojw.planner.app.system.friend.domain.dto.group.FriendGroupCreateDto;
import com.ojw.planner.app.system.friend.domain.dto.group.FriendGroupUpdateDto;
import com.ojw.planner.app.system.friend.domain.dto.request.FriendRequestCreateDto;
import com.ojw.planner.app.system.friend.service.FriendFacadeService;
import com.ojw.planner.app.system.friend.service.FriendService;
import com.ojw.planner.app.system.friend.service.group.FriendGroupService;
import com.ojw.planner.core.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

//TODO : 현재 사용자의 요청인지 검증 AOP or 서비스 공통 로직 추가
@Tag(name = "Friend", description = "친구 API")
@Validated
@RequestMapping("/friend")
@RequiredArgsConstructor
@RestController
public class FriendController {

    private final FriendFacadeService friendFacadeService;

    private final FriendGroupService friendGroupService;

    private final FriendService friendService;

    @Operation(summary = "친구 그룹 등록", tags = "Friend")
    @PostMapping(path = "/group", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFriendGroup(@RequestBody @Valid FriendGroupCreateDto createDto) {
        return new ResponseEntity<>(
                new ApiResponse<>("Friend group creation successful", friendFacadeService.createFriendGroup(createDto))
                , HttpStatus.CREATED
        );
    }

    @Operation(summary = "친구 그룹 목록 조회", tags = "Friend")
    @GetMapping(path = "/group", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findFriendGroups() {
        return new ResponseEntity<>(new ApiResponse<>(friendFacadeService.findFriendGroups()), HttpStatus.OK);
    }

    @Operation(summary = "친구 그룹 정보 수정", tags = "Friend")
    @PutMapping(path = "/group/{friendGrpId}")
    public ResponseEntity<?> updateFriendGroup(
            @RequestBody @Valid FriendGroupUpdateDto updateDto
            , @Parameter(name = "friendGrpId", required = true) @NotNull @Positive
            @PathVariable("friendGrpId") Long friendGrpId
    ) {
        friendGroupService.updateFriendGroup(friendGrpId, updateDto);
        return new ResponseEntity<>(new ApiResponse<>("Friend Group update successful"), HttpStatus.OK);
    }

    @Operation(summary = "친구 그룹 삭제", tags = "Friend")
    @DeleteMapping(path = "/group/{friendGrpId}")
    public ResponseEntity<?> deleteFriendGroup(
            @Parameter(name = "friendGrpId", required = true) @NotNull @Positive
            @PathVariable("friendGrpId") Long friendGrpId
            , @Parameter(name = "cascade", description = "하위 삭제 여부", example = "false", required = true)
            @RequestParam(value = "cascade") boolean cascade
    ) {
        friendFacadeService.deleteFriendGroup(friendGrpId, cascade);
        return new ResponseEntity<>(new ApiResponse<>("Friend Group delete successful"), HttpStatus.OK);
    }

    @Operation(summary = "친구 신청 등록", tags = "Friend")
    @PostMapping(path = "/request", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFriendRequest(@RequestBody @Valid FriendRequestCreateDto createDto) {
        return new ResponseEntity<>(
                new ApiResponse<>("Friend request creation successful", friendFacadeService.createFriendRequest(createDto))
                , HttpStatus.CREATED
        );
    }

    @Operation(summary = "친구 신청 목록 조회", tags = "Friend")
    @GetMapping(path = "/request", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findFriendRequests() {
        return new ResponseEntity<>(new ApiResponse<>(friendFacadeService.findFriendRequests()), HttpStatus.OK);
    }

    @Operation(summary = "친구 신청 승인", tags = "Friend")
    @PutMapping(path = "/request/{friendReqId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> approveFriendRequest(
            @Parameter(name = "friendReqId", required = true) @NotNull @Positive
            @PathVariable("friendReqId") Long friendReqId
            , @Parameter(name = "approve", description = "승인 여부", example = "false", required = true)
            @RequestParam(value = "approve") boolean approve
    ) {
        friendFacadeService.approveFriendRequest(friendReqId, approve);
        return new ResponseEntity<>(new ApiResponse<>("Friend request approve successful"), HttpStatus.OK);
    }

    @Operation(summary = "친구 목록 조회", tags = "Friend")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findFriends() {
        return new ResponseEntity<>(new ApiResponse<>(friendFacadeService.findFriends()), HttpStatus.OK);
    }

    @Operation(summary = "친구 정보 수정", tags = "Friend")
    @PutMapping(path = "/{friendId}")
    public ResponseEntity<?> updateFriend(
            @RequestBody @Valid FriendUpdateDto updateDto
            , @Parameter(name = "friendId", required = true) @NotNull @Positive
            @PathVariable("friendId") Long friendId
    ) {
        friendFacadeService.updateFriend(friendId, updateDto);
        return new ResponseEntity<>(new ApiResponse<>("Friend update successful"), HttpStatus.OK);
    }

    @Operation(summary = "친구 그룹 삭제", tags = "Friend")
    @DeleteMapping(path = "/{friendId}")
    public ResponseEntity<?> deleteFriend(
            @Parameter(name = "friendId", required = true) @NotNull @Positive
            @PathVariable("friendId") Long friendId
    ) {
        friendService.deleteFriend(friendId);
        return new ResponseEntity<>(new ApiResponse<>("Friend delete successful"), HttpStatus.OK);
    }

}
