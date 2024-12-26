package com.ojw.planner.core.enumeration.inner;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Expire {

    BOARD_MEMO("boardMemo:", 5L),
    BOARD_MEMO_HIT("boardMemo:hit", 5L),
    BOARD_MEMO_BACKUP_HIT("boardMemo:backup:hit", 6L);

    private String key;

    private final Long value;

}
