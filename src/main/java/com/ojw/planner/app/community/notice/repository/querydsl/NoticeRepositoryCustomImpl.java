package com.ojw.planner.app.community.notice.repository.querydsl;

import com.ojw.planner.app.community.notice.domain.Notice;
import com.ojw.planner.app.community.notice.domain.dto.NoticeFindDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ojw.planner.app.community.notice.domain.QNotice.notice;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class NoticeRepositoryCustomImpl extends NoticeRepositoryBooleanExpression implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Notice> findAll(NoticeFindDto findDto, Pageable pageable) {

        List<Notice> findNotices = queryFactory
                .selectFrom(notice)
                .where(
                        containsSearchValue(findDto.getSearchValue())
                        , notice.isDeleted.isFalse()
                )
                .orderBy(
                         notice.isTop.desc()
                        , notice.regDtm.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(findNotices, pageable, findAllCount(findDto));

    }

    private Long findAllCount(NoticeFindDto findDto) {
        return queryFactory
                .select(notice.count())
                .from(notice)
                .where(
                        containsSearchValue(findDto.getSearchValue())
                        , notice.isDeleted.isFalse()
                )
                .fetchOne();
    }

}
