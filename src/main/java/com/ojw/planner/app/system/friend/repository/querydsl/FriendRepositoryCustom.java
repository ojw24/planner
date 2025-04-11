package com.ojw.planner.app.system.friend.repository.querydsl;

import com.ojw.planner.app.system.friend.domain.Friend;

import java.util.List;

public interface FriendRepositoryCustom {

    List<Friend> findAll(String userId);

}
