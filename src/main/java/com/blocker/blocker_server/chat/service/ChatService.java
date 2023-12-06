package com.blocker.blocker_server.chat.service;

import com.blocker.blocker_server.chat.domain.ChatRoom;
import com.blocker.blocker_server.chat.domain.ChatUser;
import com.blocker.blocker_server.chat.dto.request.ChatMessageRequestDto;
import com.blocker.blocker_server.chat.dto.request.CreateChatRoomRequestDto;
import com.blocker.blocker_server.chat.dto.response.GetChatMessagesResponseDto;
import com.blocker.blocker_server.chat.dto.response.GetChatRoomListDto;
import com.blocker.blocker_server.chat.domain.ChatMessage;
import com.blocker.blocker_server.chat.mongo.ChatMessageRepository;
import com.blocker.blocker_server.commons.exception.ForbiddenException;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.commons.jwt.JwtProvider;
import com.blocker.blocker_server.chat.repository.ChatRoomRepository;
import com.blocker.blocker_server.chat.repository.ChatUserRepository;
import com.blocker.blocker_server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final JwtProvider jwtProvider;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatUserRepository chatUserRepository;
    private final ChatMessageRepository chatMessageRepository;

    public void sendMessage(String token, Long roomId, ChatMessageRequestDto message) {

        String tokenValue = token.substring(7);

        String email = jwtProvider.getEmail(tokenValue);

        //TODO: 참여자가 맞는지. ChatRoom에서 조회해야 함.
        //TODO: DB에 저장해야함.

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoomId(roomId)
                .sendAt(LocalDateTime.now())
                .sender(email)
                .content(message.getContent())
                .build();

        chatMessageRepository.save(chatMessage);

        String userName = jwtProvider.getUsername(tokenValue);

        message.setSender(userName);

        simpMessagingTemplate.convertAndSend("/sub/" + roomId, message);

    }

    public void createChatRoom(User user, CreateChatRoomRequestDto createChatRoomRequestDto) {

        ChatRoom chatRoom = ChatRoom.builder().createdAt(LocalDateTime.now()).build();

        chatRoomRepository.save(chatRoom);

        List<ChatUser> chatUsers = new ArrayList<>();

        createChatRoomRequestDto.getChatUsers().stream().forEach(email ->
                chatUsers.add(
                        ChatUser.builder()
                                .user(userRepository.getReferenceById(email))
                                .chatRoom(chatRoom)
                                .build()));

        chatUsers.add(ChatUser.builder().user(user).chatRoom(chatRoom).build());

        chatUserRepository.saveAll(chatUsers);

    }

    public List<GetChatRoomListDto> getChatRooms(User user) {

        List<GetChatRoomListDto> response = new ArrayList<>();

        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByUser(user);

        chatRooms.stream().forEach(chatRoom -> response.add(
                GetChatRoomListDto.builder()
                        .chatRoomId(chatRoom.getChatRoomID())
                        .lastChat(chatRoom.getLastChat())
                        .lastChatTime(chatRoom.getLastChatTime())
                        .build()
        ));

        return response;
    }


    public List<GetChatMessagesResponseDto> getMessages(User user, Long chatRoomId, Pageable pageable) {

        // 자신의 채팅방 아니면 채팅 내역 조회할 수 없음.
        if (!chatUserRepository.existsByUserAndChatRoom(user, chatRoomRepository.getReferenceById(chatRoomId)))
            throw new ForbiddenException("[get messages] user, chatRoomId : " + user.getEmail() + ", " + chatRoomId);

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(chatRoomId, pageable);

        List<GetChatMessagesResponseDto> response = chatMessages.stream()
                .map(chatMessage -> GetChatMessagesResponseDto.builder()
                        .sender(chatMessage.getSender())
                        .content(chatMessage.getContent())
                        .sendAt(chatMessage.getSendAt())
                        .build())
                .collect(Collectors.toList());


        return response;
    }
}
