package com.ojw.planner.app.system.friend.repository.request.querydsl;

import com.ojw.planner.app.system.friend.domain.request.FriendRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ojw.planner.app.system.friend.domain.request.QFriendRequest.friendRequest;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendRequestRepositoryCustomImpl implements FriendRequestRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FriendRequest> findAll(String userId) {
        return queryFactory
                .selectFrom(friendRequest)
                .where(
                        friendRequest.requester.userId.eq(userId)
                                .or(friendRequest.target.userId.eq(userId))
                        , friendRequest.isDeleted.isFalse()
                )
                .orderBy(friendRequest.regDtm.asc())
                .fetch();
    }

    @Override
    public Long findRequestCount(String userId, String targetId) {
        return queryFactory
                .select(friendRequest.friendReqId.count())
                .from(friendRequest)
                .where(
                        friendRequest.requester.userId.eq(userId).and(friendRequest.target.userId.eq(targetId))
                                .or(friendRequest.requester.userId.eq(targetId).and(friendRequest.target.userId.eq(userId)))
                        , friendRequest.isDeleted.isFalse()
                )
                .fetchOne();
    }

}
