package com.ojw.planner.app.community.board.domain.memo.redis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ojw.planner.app.community.board.domain.memo.BoardMemo;
import com.ojw.planner.core.enumeration.inner.Expire;
import com.ojw.planner.core.util.Utils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Setter
@Getter
@Builder
@RedisHash("boardMemo")
public class CachedBoardMemo {

    @Id
    private Long boardMemoId;

    private Long boardId;

    private String userId;

    private String userUuid;

    private String userName;

    private String profile;

    private String title;

    private String content;

    private Long hit;

    private LocalDateTime regDtm;

    private LocalDateTime updtDtm;

    @JsonIgnore
    private Boolean isDeleted;

    @JsonIgnore
    @TimeToLive(unit = TimeUnit.MINUTES)
    private Long expire;

    public static CachedBoardMemo of(BoardMemo boardMemo) {
        return CachedBoardMemo.builder()
                .boardMemoId(boardMemo.getBoardMemoId())
                .boardId(boardMemo.getBoard().getBoardId())
                .userId(Utils.maskingId(boardMemo.getUser().getUserId()))
                .userUuid(boardMemo.getUser().getUuid())
                .userName(boardMemo.getUser().getName())
                .profile(
                        boardMemo.getUser().getAttachedFile() != null
                                ? boardMemo.getUser().getAttachedFile().getPath()
                                : null
                )
                .title(boardMemo.getTitle())
                .content(boardMemo.getContent())
                .hit(boardMemo.getHit() == null ? 0 : boardMemo.getHit())
                .regDtm(boardMemo.getRegDtm())
                .updtDtm(boardMemo.getUpdtDtm())
                .isDeleted(boardMemo.getIsDeleted())
                .expire(Expire.BOARD_MEMO.getValue())
                .build();
    }

}
