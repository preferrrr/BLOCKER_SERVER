package com.blocker.blocker_server.chat.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoom is a Querydsl query type for ChatRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoom extends EntityPathBase<ChatRoom> {

    private static final long serialVersionUID = -1586895380L;

    public static final QChatRoom chatRoom = new QChatRoom("chatRoom");

    public final NumberPath<Long> chatRoomID = createNumber("chatRoomID", Long.class);

    public final ListPath<ChatUser, QChatUser> chatUsers = this.<ChatUser, QChatUser>createList("chatUsers", ChatUser.class, QChatUser.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath lastChat = createString("lastChat");

    public final DateTimePath<java.time.LocalDateTime> lastChatTime = createDateTime("lastChatTime", java.time.LocalDateTime.class);

    public QChatRoom(String variable) {
        super(ChatRoom.class, forVariable(variable));
    }

    public QChatRoom(Path<? extends ChatRoom> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatRoom(PathMetadata metadata) {
        super(ChatRoom.class, metadata);
    }

}

