package com.ojw.planner.app.community.board.domain.memo;

import com.ojw.planner.app.community.board.domain.Board;
import com.ojw.planner.app.community.board.domain.comment.BoardComment;
import com.ojw.planner.app.community.board.domain.dto.memo.BoardMemoUpdateDto;
import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "board_memo")
@Entity
public class BoardMemo extends BaseEntity {

    @Comment("게시글 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_memo_id", nullable = false)
    private Long boardMemoId;

    @Comment("게시판 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Comment("작성자 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Comment("제목")
    @Column(name = "title", nullable = false)
    private String title;

    @Comment("내용")
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Comment("조회수")
    @ColumnDefault("0")
    @Column(name = "hit", nullable = false)
    private Long hit;

    @OneToMany(mappedBy = "boardMemo")
    private List<BoardComment> comments = new ArrayList<>();

    public void update(BoardMemoUpdateDto updateDto) {

        if(StringUtils.hasText(updateDto.getTitle())) this.title = updateDto.getTitle();

        if(StringUtils.hasText(updateDto.getContent())) this.content = updateDto.getContent();

    }

    public void delete() {
        this.isDeleted = true;
    }

}
