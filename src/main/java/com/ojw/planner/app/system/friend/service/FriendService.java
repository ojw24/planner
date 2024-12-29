package com.ojw.planner.app.system.friend.service;

import com.ojw.planner.app.system.friend.domain.Friend;
import com.ojw.planner.app.system.friend.domain.dto.FriendDto;
import com.ojw.planner.app.system.friend.domain.dto.FriendUpdateDto;
import com.ojw.planner.app.system.friend.domain.group.FriendGroup;
import com.ojw.planner.app.system.friend.repository.FriendRepository;
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
public class FriendService {

    private final FriendRepository friendRepository;

    /**
     * 친구 등록
     *
     * @param friend - 친구
     * @return 생성된 친구 아이디
     */
    @Transactional
    public Long createFriend(Friend friend) {
        return friendRepository.save(friend).getFriendId();
    }

    /**
     * 친구 목록 조회
     * 
     * @param userId - 사용자 아이디
     * @return 친구 목록
     */
    public List<FriendDto> findFriends(String userId) {
        return friendRepository.findAll(userId)
                .stream()
                .map(FriendDto::of)
                .collect(Collectors.toList());
    }

    protected Friend getFriend(Long friendId, String userId) {
        return friendRepository.findByFriendIdAndUserUserId(friendId, userId)
                .orElseThrow(() -> new ResponseException("not exist friend : " + friendId, HttpStatus.NOT_FOUND));
    }

    /**
     * 마지막 순서 조회
     *
     * @param userId - 사용자 아이디
     * @return 마지막 순서
     */
    protected Double getLastOrder(String userId) {
        Double order = friendRepository.getLastOrder(userId);
        return order == null ? 1 : order + 1;
    }

    /**
     * 친구 수정
     *
     * @param friendId    - 친구 아이디
     * @param updateDto   - 수정 정보
     * @param friendGroup - 친구 그룹
     */
    @Transactional
    public void updateFriend(
            Long friendId
            , String userId
            , FriendUpdateDto updateDto
            , FriendGroup friendGroup
    ) {
        Friend updateFriend = getFriend(friendId, userId);
        updateFriend.update(updateDto, friendGroup);
    }

    @Transactional
    public void deleteFriend(Long friendId, String userId) {
        friendRepository.deleteByFriendIdAndUserUserId(friendId, userId);
    }

}
