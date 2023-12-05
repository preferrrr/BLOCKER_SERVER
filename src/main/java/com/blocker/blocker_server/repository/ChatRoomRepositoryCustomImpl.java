package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.*;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QChatRoom chatRoom = QChatRoom.chatRoom;
    QUser user = QUser.user;
    QChatUser chatUser = QChatUser.chatUser;

    @Override
    public List<ChatRoom> findChatRoomsByUser(User user) {

        JPAQuery<ChatRoom> getChatRoomListQuery = jpaQueryFactory
                .selectFrom(chatRoom)
                .distinct()
                .join(chatRoom.chatUsers, chatUser).fetchJoin()
                .where(chatUser.user.email.eq(user.getEmail()));

        List<ChatRoom> result = getChatRoomListQuery.fetch();

        return result;
    }
}
