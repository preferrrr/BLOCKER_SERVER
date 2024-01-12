package com.blocker.blocker_server.commons.config;

import com.blocker.blocker_server.chat.domain.ChatRoom;
import com.blocker.blocker_server.chat.repository.ChatRoomRepository;
import com.blocker.blocker_server.chat.repository.ChatUserRepository;
import com.blocker.blocker_server.commons.exception.ForbiddenException;
import com.blocker.blocker_server.commons.jwt.JwtProvider;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
@Slf4j
public class ChatPreHandler implements ChannelInterceptor {

    private final JwtProvider jwtProvider;
    private final ChatUserRepository chatUserRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    private final String SEND_URL = "/pub/message/";
    private final String SUBSCRIBE_URL = "/sub/";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        //소켓을 사용하려면 USER 권한을 가져야함.
        String token = getToken(accessor);

        //토큰 유효성 검사
        boolean isValid = jwtProvider.isTokenValid(token);
        boolean isUser = jwtProvider.getRoles(token).stream().anyMatch(role -> role.equals("USER"));

        if(!isUser || !isValid)
            throw new MessageDeliveryException("unauthorized");

        StompCommand command = accessor.getCommand();

        //SEND와 SUBSCRIBE은 그 방의 참여자여야 가능함.
        if (command.equals(StompCommand.SEND) || command.equals(StompCommand.SUBSCRIBE)) {
            isParticipant(getChatRoomId(accessor), token);
        }

        return message;

    }

    private String getToken(StompHeaderAccessor accessor) {
        String token = String.valueOf(accessor.getNativeHeader("Authorization"));

        if (token == null || token.isEmpty())
            throw new MessageDeliveryException("token is null");
        else if (token.charAt(0) == '[')
            return token.substring(8, token.length() - 1);
        else
            return token.substring(7);

    }

    private Long getChatRoomId(StompHeaderAccessor accessor) {

        try {
            if(accessor.getCommand().equals(StompCommand.SEND))
                return Long.parseLong(accessor.getDestination().substring(SEND_URL.length()));
            else
                return Long.parseLong(accessor.getDestination().substring(SUBSCRIBE_URL.length()));
        } catch (Exception e) {
            throw new MessageDeliveryException("fail parseLong : " + accessor.getDestination());
        }

    }

    private void isParticipant(Long chatRoomId, String token) {

        ChatRoom chatRoom = chatRoomRepository.getReferenceById(chatRoomId);
        User user = userRepository.getReferenceById(jwtProvider.getEmail(token));

        if (!chatUserRepository.existsByUserAndChatRoom(user, chatRoom))
            throw new ForbiddenException("not participant");

    }

}

