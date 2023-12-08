package com.blocker.blocker_server.commons.config;

import com.blocker.blocker_server.chat.domain.ChatRoom;
import com.blocker.blocker_server.chat.repository.ChatRoomRepository;
import com.blocker.blocker_server.chat.repository.ChatUserRepository;
import com.blocker.blocker_server.commons.exception.ForbiddenException;
import com.blocker.blocker_server.commons.jwt.JwtProvider;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@RequiredArgsConstructor
public class ChatPreHandler implements ChannelInterceptor {

    private final JwtProvider jwtProvider;
    private final ChatUserRepository chatUserRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        //소켓을 사용하려면 USER 권한을 가져야함.
        String token = getToken(accessor);

        if (token == null)
            throw new MessageDeliveryException("token is null");

        //토큰 유효성 검사
        boolean isValid = jwtProvider.isTokenValid(token);
        boolean isUser = jwtProvider.getRoles(token).stream().anyMatch(role -> role.equals("USER"));

        if(!isUser || !isValid)
            throw new MessageDeliveryException("unauthorized");

        StompCommand command = accessor.getCommand();

        //SEND와 SUBSCRIBE은 그 방의 참여자여야 가능함.
        if (command.equals(StompCommand.SEND) || command.equals(StompCommand.SUBSCRIBE)) {

            Long chatRoomId = getChatRoomId(accessor);

            if (chatRoomId == null)
                throw new MessageDeliveryException("ChatRoomId is null");

            ChatRoom chatRoom = chatRoomRepository.getReferenceById(chatRoomId);
            User user = userRepository.getReferenceById(jwtProvider.getEmail(token));

            if (!chatUserRepository.existsByUserAndChatRoom(user, chatRoom))
                throw new ForbiddenException("not participant");

        }

        return message;

    }

    private String getToken(StompHeaderAccessor accessor) {
        String token = String.valueOf(accessor.getNativeHeader("Authorization"));

        if (token == null || token.isEmpty())
            return null;
        else if (token.charAt(0) == '[')
            return token.substring(8, token.length() - 1);
        else
            return token.substring(7);

    }

    private Long getChatRoomId(StompHeaderAccessor accessor) {
        String chatRoomId = String.valueOf(accessor.getNativeHeader("ChatRoomId"));

        if (chatRoomId == null || chatRoomId.isEmpty())
            return null;
        else if (chatRoomId.charAt(0) == '[')
            return Long.parseLong(chatRoomId.substring(1, chatRoomId.length() - 1));
        else
            return Long.parseLong(chatRoomId);

    }

}

