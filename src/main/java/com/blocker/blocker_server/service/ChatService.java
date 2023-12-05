package com.blocker.blocker_server.service;

import com.blocker.blocker_server.dto.request.ChatMessage;
import com.blocker.blocker_server.dto.request.CreateChatRoomDto;
import com.blocker.blocker_server.dto.response.GetChatRoomsDto;
import com.blocker.blocker_server.entity.ChatRoom;
import com.blocker.blocker_server.entity.ChatUser;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.jwt.JwtProvider;
import com.blocker.blocker_server.repository.ChatRoomRepository;
import com.blocker.blocker_server.repository.ChatUserRepository;
import com.blocker.blocker_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatUserRepository chatUserRepository;

    public void sendMessage(String token, Long roomId, ChatMessage message) {

        String tokenValue = token.substring(7);

        String email = jwtProvider.getEmail(tokenValue);

        //TODO: 참여자가 맞는지. ChatRoom에서 조회해야 함.
        //TODO: DB에 저장해야함.

        String userName = jwtProvider.getUsername(tokenValue);

        message.setSender(userName);

        simpMessagingTemplate.convertAndSend("/sub/" + roomId, message);

    }

    public void createChatRoom(User user, CreateChatRoomDto createChatRoomDto) {

        ChatRoom chatRoom = ChatRoom.builder().createdAt(LocalDateTime.now()).build();

        chatRoomRepository.save(chatRoom);

        List<ChatUser> chatUsers = new ArrayList<>();

        createChatRoomDto.getChatUsers().stream().forEach(email ->
                chatUsers.add(
                        ChatUser.builder()
                                .user(userRepository.getReferenceById(email))
                                .chatRoom(chatRoom)
                                .build()));

        chatUsers.add(ChatUser.builder().user(user).chatRoom(chatRoom).build());

        chatUserRepository.saveAll(chatUsers);

    }

    public List<GetChatRoomsDto> getChatRooms(User user) {

        List<GetChatRoomsDto> response = new ArrayList<>();

        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByUser(user);

        chatRooms.stream().forEach(chatRoom -> response.add(
                GetChatRoomsDto.builder()
                        .chatRoomId(chatRoom.getChatRoomID())
                        .lastChat(chatRoom.getLastChat())
                        .lastChatTime(chatRoom.getLastChatTime())
                        .build()
        ));

        return response;
    }
}
