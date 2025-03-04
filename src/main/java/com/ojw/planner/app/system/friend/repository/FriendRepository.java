package com.ojw.planner.app.system.friend.repository;

import com.ojw.planner.app.system.friend.domain.Friend;
import com.ojw.planner.app.system.friend.repository.querydsl.FriendRepositoryCustom;
import com.ojw.planner.app.system.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long>, FriendRepositoryCustom {

    Optional<Friend> findByFriendIdAndUserUserId(Long friendId, String userId);

    Optional<Friend> findByUserAndFriend(User user, User friend);

    void deleteByFriendIdAndUserUserId(Long friendId, String userId);

}
