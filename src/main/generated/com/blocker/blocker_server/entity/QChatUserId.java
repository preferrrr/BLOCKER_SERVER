package com.blocker.blocker_server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QChatUserId is a Querydsl query type for ChatUserId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QChatUserId extends BeanPath<ChatUserId> {

    private static final long serialVersionUID = -1466239292L;

    public static final QChatUserId chatUserId = new QChatUserId("chatUserId");

    public final NumberPath<Long> chatRoomId = createNumber("chatRoomId", Long.class);

    public final StringPath email = createString("email");

    public QChatUserId(String variable) {
        super(ChatUserId.class, forVariable(variable));
    }

    public QChatUserId(Path<? extends ChatUserId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatUserId(PathMetadata metadata) {
        super(ChatUserId.class, metadata);
    }

}

