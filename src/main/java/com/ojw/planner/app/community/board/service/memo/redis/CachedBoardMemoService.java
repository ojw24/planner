package com.ojw.planner.app.community.board.service.memo.redis;

import com.ojw.planner.app.community.board.domain.memo.redis.CachedBoardMemo;
import com.ojw.planner.app.community.board.repository.memo.redis.CachedBoardMemoRepository;
import com.ojw.planner.core.enumeration.inner.Expire;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.ojw.planner.core.enumeration.inner.Expire.BOARD_MEMO_HIT;
import static com.ojw.planner.core.enumeration.inner.Expire.BOARD_MEMO_BACKUP_HIT;

@RequiredArgsConstructor
@Service
public class CachedBoardMemoService {

    private final CachedBoardMemoRepository cachedBoardMemoRepository;

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 게시글 저장
     *
     * @param boardMemo - 게시글
     * @return 저장된 게시글 아이디
     */
    public Long saveBoardMemo(CachedBoardMemo boardMemo) {
        return cachedBoardMemoRepository.save(boardMemo).getBoardMemoId();
    }

    public Optional<CachedBoardMemo> getBoardMemo(Long boardMemoId) {
        return cachedBoardMemoRepository.findById(boardMemoId);
    }

    /**
     * 조회수 증가
     *
     * @param boardMemoId - 게시글 아이디
     * @return 증가된 조회수
     */
    public Long plusHit(Long boardMemoId, Long hit) {
        incrementHit(BOARD_MEMO_BACKUP_HIT, boardMemoId, hit);
        return incrementHit(BOARD_MEMO_HIT, boardMemoId, hit);
    }

    private Long incrementHit(Expire expire, Long boardMemoId, Long hit) {

        redisTemplate.opsForValue().setIfAbsent(
                expire.getKey() + boardMemoId
                , hit.toString()
                , expire.getValue()
                , TimeUnit.MINUTES
        );

        return redisTemplate.opsForValue().increment(expire.getKey() + boardMemoId, 1);

    }


    /**
     * 게시글 삭제
     *
     * @param boardMemoId - 게시글 아이디
     */
    public void deleteBoardMemo(Long boardMemoId) {
        cachedBoardMemoRepository.deleteById(boardMemoId);
        redisTemplate.delete(BOARD_MEMO_HIT.getKey() + boardMemoId);
        redisTemplate.delete(BOARD_MEMO_BACKUP_HIT.getKey() + boardMemoId);
    }

}
