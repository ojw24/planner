package com.ojw.planner.listener;

import com.ojw.planner.app.community.board.repository.memo.BoardMemoRepository;
import com.ojw.planner.app.community.board.service.memo.redis.CachedBoardMemoService;
import com.ojw.planner.core.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static com.ojw.planner.core.enumeration.inner.Expire.BOARD_MEMO_HIT;
import static com.ojw.planner.core.enumeration.inner.Expire.BOARD_MEMO_BACKUP_HIT;

@RequiredArgsConstructor
@Component
public class RedisExpiredListener implements MessageListener {

    private final RedisTemplate<String, String> redisTemplate;

    private final BoardMemoRepository boardMemoRepository;

    private final CachedBoardMemoService cachedBoardMemoService;

    @Transactional
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = new String(message.getBody());
        syncBoardMemoHit(key);
    }

    /**
     * 게시글 조회수 동기화
     *
     * 만료된 조회수 키 replace하여 백업 조회수 조회(백업 데이터는 유효시간 남아있는 상태)
     * 백업 조회수를 통해 DB에 게시글 조회수 동기화
     */
    private void syncBoardMemoHit(String key) {

        if(key.contains(BOARD_MEMO_HIT.getKey())) {

            String id = key.replace(BOARD_MEMO_HIT.getKey(), "");
            String backup = key.replace(BOARD_MEMO_HIT.getKey(), BOARD_MEMO_BACKUP_HIT.getKey());
            String hitVal = redisTemplate.opsForValue().getAndDelete(backup);
            if(StringUtils.hasText(hitVal) && Utils.checkIntegral(hitVal)) {

                Long hit = Long.parseLong(hitVal);
                if(StringUtils.hasText(id) && Utils.checkIntegral(id)) {

                    Long idl = Long.parseLong(id);
                    boardMemoRepository.updateHit(idl, hit);
                    cachedBoardMemoService.getBoardMemo(idl)
                            .ifPresent(e -> {
                                e.setHit(hit);
                                cachedBoardMemoService.saveBoardMemo(e);
                            });

                }

            }

        }

    }

}
