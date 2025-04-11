package com.ojw.planner.app.system.friend.service;

import com.ojw.planner.app.system.friend.domain.Friend;
import com.ojw.planner.app.system.friend.domain.dto.FriendDto;
import com.ojw.planner.app.system.friend.domain.group.FriendGroup;
import com.ojw.planner.app.system.friend.repository.FriendRepository;
import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

    protected Optional<Friend> getFriend(User user, User friend) {
        return friendRepository.findByUserAndFriend(user, friend);
    }

    /**
     * 친구 수정
     *
     * @param friendId    - 친구 아이디
     * @param friendGroup - 친구 그룹
     */
    @Transactional
    public void updateFriend(
            Long friendId
            , String userId
            , FriendGroup friendGroup
    ) {
        Friend updateFriend = getFriend(friendId, userId);
        updateFriend.update(friendGroup);
    }

    @Transactional
    public void deleteFriend(Long friendId, String userId) {
        friendRepository.deleteByFriendIdAndUserUserId(friendId, userId);
    }

}
