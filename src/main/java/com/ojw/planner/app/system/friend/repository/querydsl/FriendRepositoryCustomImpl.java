package com.ojw.planner.app.system.friend.repository.querydsl;

import com.ojw.planner.app.system.friend.domain.Friend;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ojw.planner.app.system.friend.domain.QFriend.friend1;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendRepositoryCustomImpl implements FriendRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Friend> findAll(String userId) {
        return queryFactory
                .selectFrom(friend1)
                .where(
                        friend1.user.userId.eq(userId)
                        , friend1.friendGroup.isNull()
                )
                .orderBy(friend1.friend.name.asc())
                .fetch();
    }

}
