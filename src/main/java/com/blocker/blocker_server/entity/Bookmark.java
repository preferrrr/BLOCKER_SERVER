package com.blocker.blocker_server.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Table(name = "BOOKMARK")
@DynamicInsert
public class Bookmark {

    @EmbeddedId
    private BookmarkId id;

    @MapsId("email")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    private User user;

    @MapsId("boardId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board")
    private Board board;

    @Builder
    public Bookmark(String email, Long boardId, User user, Board board) {
        BookmarkId id = BookmarkId.builder()
                .boardId(boardId)
                .email(email)
                .build();

        this.id = id;
        this.board = board;
        this.user = user;
    }

}
