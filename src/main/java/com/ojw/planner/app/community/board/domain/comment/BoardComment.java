package com.ojw.planner.app.community.board.domain.comment;

import com.ojw.planner.app.community.board.domain.dto.comment.BoardCommentUpdateDto;
import com.ojw.planner.app.community.board.domain.memo.BoardMemo;
import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
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
@Table(name = "board_comment")
@Entity
public class BoardComment extends BaseEntity {

    @Comment("댓글 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_comment_id", nullable = false)
    private Long boardCommentId;

    @Comment("게시글 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_memo_id", nullable = false, updatable = false)
    private BoardMemo boardMemo;

    @Comment("작성자 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Comment("상위 댓글 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id", updatable = false)
    private BoardComment parent;

    @Comment("내용")
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Comment("최상위 댓글 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_comment_id", updatable = false)
    private BoardComment root;

    @OneToMany(mappedBy = "parent")
    private List<BoardComment> children = new ArrayList<>();

    public void update(BoardCommentUpdateDto updateDto) {

        if(StringUtils.hasText(updateDto.getContent())) this.content = updateDto.getContent();

    }

    public void delete() {
        this.isDeleted = true;
    }

}
