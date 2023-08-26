package com.blocker.blocker_server.entity;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BookmarkId implements Serializable {

    private String email;

    private Long boardId;

    @Builder
    public BookmarkId(String email, Long boardId) {
        this.email = email;
        this.boardId = boardId;
    }

}