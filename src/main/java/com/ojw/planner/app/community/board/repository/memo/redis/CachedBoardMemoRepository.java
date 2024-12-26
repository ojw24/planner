package com.ojw.planner.app.community.board.repository.memo.redis;

import com.ojw.planner.app.community.board.domain.memo.redis.CachedBoardMemo;
import org.springframework.data.repository.CrudRepository;

public interface CachedBoardMemoRepository extends CrudRepository<CachedBoardMemo, Long> {
}
