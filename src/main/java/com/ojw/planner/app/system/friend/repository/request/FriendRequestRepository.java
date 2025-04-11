package com.ojw.planner.app.system.friend.repository.request;

import com.ojw.planner.app.system.friend.domain.request.FriendRequest;
import com.ojw.planner.app.system.friend.repository.request.querydsl.FriendRequestRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long>, FriendRequestRepositoryCustom {
}
