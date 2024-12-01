package com.ojw.planner.app.system.user.repository.querydsl;

import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.app.system.user.domain.dto.UserFindDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ojw.planner.app.system.user.domain.QUser.user;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class UserRepositoryCustomImpl
        extends UserRepositoryBooleanExpression
        implements UserRepositoryCustom
{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<User> findAll(UserFindDTO findDTO, Pageable pageable) {

        List<User> findUsers = queryFactory
                .selectFrom(user)
                .where(
                        containsSearchValue(findDTO.getSearchType(), findDTO.getSearchValue())
                        , user.isDeleted.isFalse()
                )
                .orderBy(user.regDtm.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(findUsers, pageable, findAllCount(findDTO));

    }

    private Long findAllCount(UserFindDTO findDTO) {
        return queryFactory
                .select(user.count())
                .from(user)
                .where(
                        containsSearchValue(findDTO.getSearchType(), findDTO.getSearchValue())
                        , user.isDeleted.isFalse()
                )
                .fetchOne();
    }

}
