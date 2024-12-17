package com.ojw.planner.app.system.friend.repository;

import com.ojw.planner.app.system.friend.domain.Friend;
import com.ojw.planner.app.system.friend.repository.querydsl.FriendRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long>, FriendRepositoryCustom {
}
