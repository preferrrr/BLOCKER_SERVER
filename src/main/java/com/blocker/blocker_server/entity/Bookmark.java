package com.blocker.blocker_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
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

}
