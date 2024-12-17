package com.ojw.planner.app.system.friend.repository.request.querydsl;

import com.ojw.planner.app.system.friend.domain.request.FriendRequest;

import java.util.List;

public interface FriendRequestRepositoryCustom {

    List<FriendRequest> findAll(String userId);

    Long findRequestCount(String userId, String targetId);

}
