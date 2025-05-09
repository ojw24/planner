package com.ojw.planner.app.system.friend.service.request;

import com.ojw.planner.app.system.friend.domain.dto.request.FriendRequestCreateDto;
import com.ojw.planner.app.system.friend.domain.dto.request.FriendRequestDto;
import com.ojw.planner.app.system.friend.domain.request.FriendRequest;
import com.ojw.planner.app.system.friend.repository.request.FriendRequestRepository;
import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    @Transactional
    public FriendRequest createFriendRequest(
            FriendRequestCreateDto createDto
            , User requester
            , User target
    ) {

        if(friendRequestRepository.findRequestCount(requester.getUserId(), target.getUserId()) > 0)
            throw new ResponseException("이미 신청되었습니다.", HttpStatus.CONFLICT);

        return friendRequestRepository.save(createDto.toEntity(requester, target));

    }

    public FriendRequest getFriendRequest(Long friendGrpId) {
        return friendRequestRepository.findById(friendGrpId)
                .orElseThrow(() -> new ResponseException(
                        "not exist friend request : " + friendGrpId
                        , HttpStatus.NOT_FOUND
                ));
    }

    /**
     * 친구 신청 목록 조회
     *
     * @param userId - 사용자 아이디
     * @return 친구 신청 정보 목록
     */
    public List<FriendRequestDto> findFriendRequests(String userId) {
        return getAll(userId).stream()
                .map(f -> FriendRequestDto.of(f, userId))
                .collect(Collectors.toList());
    }

    private List<FriendRequest> getAll(String userId) {
        return friendRequestRepository.findAll(userId);
    }

    /**
     * 친구 신청 삭제
     *
     * @param friendReqId - 친구 신청 아이디
     */
    @Transactional
    public void deleteFriendRequest(Long friendReqId) {
        getFriendRequest(friendReqId).delete();
    }

}
