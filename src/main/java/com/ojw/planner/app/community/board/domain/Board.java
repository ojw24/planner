package com.ojw.planner.app.community.board.domain;

import com.ojw.planner.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@SuperBuilder
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "board")
@Entity
public class Board extends BaseEntity {

    @Comment("게시판 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Comment("제목")
    @Column(name = "title", nullable = false)
    private String title;

    @Comment("순서")
    @Column(name = "ord", nullable = false)
    private Double ord;

}
