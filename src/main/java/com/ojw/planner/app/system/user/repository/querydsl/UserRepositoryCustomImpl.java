package com.ojw.planner.app.system.user.repository.querydsl;

import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.app.system.user.domain.dto.UserDto.UserSimpleDto;
import com.ojw.planner.app.system.user.domain.dto.UserFindDto;
import com.querydsl.core.types.Projections;
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
    public Page<User> findAll(UserFindDto findDto, Pageable pageable) {

        List<User> findUsers = queryFactory
                .selectFrom(user)
                .where(
                        containsSearchValue(findDto.getSearchType(), findDto.getSearchValue())
                        , user.isDeleted.isFalse()
                )
                .orderBy(user.regDtm.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(findUsers, pageable, findAllCount(findDto));

    }

    private Long findAllCount(UserFindDto findDto) {
        return queryFactory
                .select(user.count())
                .from(user)
                .where(
                        containsSearchValue(findDto.getSearchType(), findDto.getSearchValue())
                        , user.isDeleted.isFalse()
                )
                .fetchOne();
    }

    @Override
    public List<UserSimpleDto> findSimples(UserFindDto findDto, String userId) {
        return queryFactory
                .select(
                        Projections.fields(
                                UserSimpleDto.class
                                , user.userId
                                , user.name
                                , user.isBanned
                        )
                )
                .from(user)
                .where(
                        containsSearchValue(findDto.getSearchType(), findDto.getSearchValue())
                        , user.userId.ne(userId)
                        , user.isDeleted.isFalse()
                )
                .orderBy(user.regDtm.desc())
                .fetch();
    }

}
