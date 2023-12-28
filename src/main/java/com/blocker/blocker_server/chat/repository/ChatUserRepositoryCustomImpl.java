package com.blocker.blocker_server.chat.repository;

import com.blocker.blocker_server.chat.domain.ChatRoom;
import com.blocker.blocker_server.chat.domain.QChatRoom;
import com.blocker.blocker_server.chat.domain.QChatUser;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatUserRepositoryCustomImpl implements ChatUserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QChatUser chatUser = QChatUser.chatUser;
    QChatRoom chatRoom = QChatRoom.chatRoom;

    @Override
    public Long findChatRoomByUsers(String userA, String userB) {

        JPAQuery<Long> getChatRoomIdQuery = jpaQueryFactory
                .select(chatRoom.chatRoomID)
                .from(chatUser)
                .where(chatUser.user.email.in(userA, userB))
                .groupBy(chatUser.chatRoom.chatRoomID)
                .having(chatUser.user.email.countDistinct().eq(2L)
                        .and(chatUser.chatRoom.chatRoomID.notIn(
                                JPAExpressions.select(chatUser.chatRoom.chatRoomID)
                                        .from(chatUser)
                                        .where(chatUser.user.email.notIn(userA, userB))
                                        .groupBy(chatUser.chatRoom.chatRoomID)
                        )));

        Long chatRoomId = getChatRoomIdQuery.fetchFirst();

        return chatRoomId;
    }
}
