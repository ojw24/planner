package com.ojw.planner.app.system.friend.repository.group.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.ojw.planner.app.system.friend.domain.group.QFriendGroup.friendGroup;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendGroupRepositoryCustomImpl implements FriendGroupRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Double getLastOrder(String userId) {
        return queryFactory
                .select(friendGroup.ord)
                .from(friendGroup)
                .where(friendGroup.user.userId.eq(userId))
                .orderBy(friendGroup.ord.desc())
                .fetchFirst();
    }

}
