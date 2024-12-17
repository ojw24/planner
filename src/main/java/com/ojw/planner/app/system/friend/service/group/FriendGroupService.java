package com.ojw.planner.app.system.friend.service.group;

import com.ojw.planner.app.system.friend.domain.dto.group.FriendGroupCreateDto;
import com.ojw.planner.app.system.friend.domain.dto.group.FriendGroupDto;
import com.ojw.planner.app.system.friend.domain.dto.group.FriendGroupUpdateDto;
import com.ojw.planner.app.system.friend.domain.group.FriendGroup;
import com.ojw.planner.app.system.friend.repository.group.FriendGroupRepository;
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
public class FriendGroupService {

    private final FriendGroupRepository friendGroupRepository;

    /**
     * 친구 그룹 등록
     *
     * @param createDto - 등록 정보
     * @param user      - 사용자
     * @return 생성된 친구 그룹 아이디
     */
    @Transactional
    public Long createFriendGroup(FriendGroupCreateDto createDto, User user) {
        return friendGroupRepository.save(createDto.toEntity(
                user
                , getLastOrder(user.getUserId())
        )).getFriendGrpId();
    }

    /**
     * 친구 그룹 목록 조회
     *
     * @param user - 사용자
     * @return 친구 그룹 정보 목록
     */
    public List<FriendGroupDto> findFriendGroups(User user) {
        return friendGroupRepository.findAllByUserOrderByOrd(user)
                .stream().map(FriendGroupDto::of).collect(Collectors.toList());
    }

    public FriendGroup getFriendGroup(Long friendGrpId) {
        return friendGroupRepository.findById(friendGrpId)
                .orElseThrow(() -> new ResponseException(
                        "not exist friend group : " + friendGrpId
                        , HttpStatus.NOT_FOUND
                ));
    }

    /**
     * 마지막 순서 조회
     *
     * @param userId - 사용자 아이디
     * @return 마지막 순서
     */
    protected Double getLastOrder(String userId) {
        Double order = friendGroupRepository.getLastOrder(userId);
        return order == null ? 1 : order + 1;
    }

    /**
     * 친구 그룹 수정
     *
     * @param friendGrpId - 친구 그룹 아이디
     * @param updateDto   - 수정 정보
     */
    @Transactional
    public void updateFriendGroup(Long friendGrpId, FriendGroupUpdateDto updateDto) {
        FriendGroup updateGroup = getFriendGroup(friendGrpId);
        updateGroup.update(updateDto);
    }

    /**
     * 친구 그룹 삭제
     *
     * @param friendGrpId - 친구 그룹 아이디
     */
    @Transactional
    public void deleteFriendGroup(Long friendGrpId) {
        friendGroupRepository.delete(getFriendGroup(friendGrpId));
    }

}
