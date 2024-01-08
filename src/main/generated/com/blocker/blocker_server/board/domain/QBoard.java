package com.blocker.blocker_server.board.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = 313847433L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoard board = new QBoard("board");

    public final com.blocker.blocker_server.commons.QBaseEntity _super = new com.blocker.blocker_server.commons.QBaseEntity(this);

    public final NumberPath<Long> boardId = createNumber("boardId", Long.class);

    public final NumberPath<Integer> bookmarkCount = createNumber("bookmarkCount", Integer.class);

    public final ListPath<com.blocker.blocker_server.bookmark.domain.Bookmark, com.blocker.blocker_server.bookmark.domain.QBookmark> bookmarks = this.<com.blocker.blocker_server.bookmark.domain.Bookmark, com.blocker.blocker_server.bookmark.domain.QBookmark>createList("bookmarks", com.blocker.blocker_server.bookmark.domain.Bookmark.class, com.blocker.blocker_server.bookmark.domain.QBookmark.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final com.blocker.blocker_server.contract.domain.QContract contract;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<com.blocker.blocker_server.Image.domain.Image, com.blocker.blocker_server.Image.domain.QImage> images = this.<com.blocker.blocker_server.Image.domain.Image, com.blocker.blocker_server.Image.domain.QImage>createList("images", com.blocker.blocker_server.Image.domain.Image.class, com.blocker.blocker_server.Image.domain.QImage.class, PathInits.DIRECT2);

    public final StringPath info = createString("info");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath representImage = createString("representImage");

    //inherited
    public final StringPath status = _super.status;

    public final StringPath title = createString("title");

    public final com.blocker.blocker_server.user.domain.QUser user;

    public final NumberPath<Integer> view = createNumber("view", Integer.class);

    public QBoard(String variable) {
        this(Board.class, forVariable(variable), INITS);
    }

    public QBoard(Path<? extends Board> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoard(PathMetadata metadata, PathInits inits) {
        this(Board.class, metadata, inits);
    }

    public QBoard(Class<? extends Board> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contract = inits.isInitialized("contract") ? new com.blocker.blocker_server.contract.domain.QContract(forProperty("contract"), inits.get("contract")) : null;
        this.user = inits.isInitialized("user") ? new com.blocker.blocker_server.user.domain.QUser(forProperty("user")) : null;
    }

}

