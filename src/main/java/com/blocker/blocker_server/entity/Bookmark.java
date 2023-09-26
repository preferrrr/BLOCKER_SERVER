package com.blocker.blocker_server.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Table(name = "BOOKMARK")
@DynamicInsert
@NoArgsConstructor
public class Bookmark {

    @EmbeddedId
    private BookmarkId id;

    @MapsId("email")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("boardId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Bookmark(User user, Board board) {
        BookmarkId id = BookmarkId.builder()
                .boardId(board.getBoardId())
                .email(user.getEmail())
                .build();

        this.id = id;
        this.board = board;
        this.user = user;
    }

}
