package com.blocker.blocker_server.config;

import com.blocker.blocker_server.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ChatPreHandler implements ChannelInterceptor {

    private final JwtProvider jwtProvider;
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        String authorizationHeader = String.valueOf(accessor.getNativeHeader("Authorization"));

        if(accessor.getCommand().equals(StompCommand.CONNECT) || accessor.getCommand().equals(StompCommand.SEND)) {

            if(authorizationHeader == null || authorizationHeader.isEmpty()){
                throw new MessageDeliveryException("message error");
            }
            String token = authorizationHeader.substring(BEARER_PREFIX.length());
            jwtProvider.isTokenValid(token);

        }

        return message;

    }
}

