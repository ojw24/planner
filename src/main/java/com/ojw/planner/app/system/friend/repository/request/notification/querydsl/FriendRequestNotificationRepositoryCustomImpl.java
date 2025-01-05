package com.ojw.planner.app.system.friend.repository.request.notification.querydsl;

import com.ojw.planner.app.system.friend.domain.request.notification.FriendRequestNotification;
import com.ojw.planner.core.enumeration.system.friend.NotificationType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ojw.planner.app.system.friend.domain.request.QFriendRequest.friendRequest;
import static com.ojw.planner.app.system.friend.domain.request.notification.QFriendRequestNotification.friendRequestNotification;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendRequestNotificationRepositoryCustomImpl implements FriendRequestNotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FriendRequestNotification> findAll(String userId) {
        return queryFactory
                .selectFrom(friendRequestNotification)
                .join(friendRequest)
                .on(friendRequestNotification.request.eq(friendRequest))
                .where(
                        (
                                friendRequestNotification.notiType.eq(NotificationType.REQUEST)
                                .and(friendRequest.target.userId.equalsIgnoreCase(userId))
                        )
                        .or(
                             friendRequestNotification.notiType.ne(NotificationType.REQUEST)
                             .and(friendRequest.requester.userId.equalsIgnoreCase(userId))
                        )
                )
                .orderBy(friendRequestNotification.regDtm.desc())
                .fetch();
    }

}
