package com.ojw.planner.app.community.notice.repository.querydsl;

import com.ojw.planner.app.community.notice.domain.Notice;
import com.ojw.planner.app.community.notice.domain.dto.NoticeFindDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

    Page<Notice> findAll(NoticeFindDto findDto, Pageable pageable);

}
