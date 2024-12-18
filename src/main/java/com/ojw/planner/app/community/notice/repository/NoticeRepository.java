package com.ojw.planner.app.community.notice.repository;

import com.ojw.planner.app.community.notice.domain.Notice;
import com.ojw.planner.app.community.notice.repository.querydsl.NoticeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {

    Optional<Notice> findByNoticeIdAndIsDeletedIsFalse(Long noticeId);

}
