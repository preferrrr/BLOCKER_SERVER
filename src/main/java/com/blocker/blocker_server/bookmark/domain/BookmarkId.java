package com.blocker.blocker_server.bookmark.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class BookmarkId implements Serializable {

    private String email;

    private Long boardId;

    @Builder
    public BookmarkId(String email, Long boardId) {
        this.email = email;
        this.boardId = boardId;
    }

}
