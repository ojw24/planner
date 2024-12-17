package com.ojw.planner.app.system.friend.repository.group;

import com.ojw.planner.app.system.friend.domain.group.FriendGroup;
import com.ojw.planner.app.system.friend.repository.group.querydsl.FriendGroupRepositoryCustom;
import com.ojw.planner.app.system.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendGroupRepository extends JpaRepository<FriendGroup, Long>, FriendGroupRepositoryCustom {

    List<FriendGroup> findAllByUserOrderByOrd(User user);

}
