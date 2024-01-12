package com.blocker.blocker_server.chat.service;

import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.board.repository.BoardRepository;
import com.blocker.blocker_server.board.service.BoardServiceSupport;
import com.blocker.blocker_server.chat.domain.ChatMessage;
import com.blocker.blocker_server.chat.domain.ChatRoom;
import com.blocker.blocker_server.chat.domain.ChatUser;
import com.blocker.blocker_server.chat.dto.response.GetChatMessagesResponseDto;
import com.blocker.blocker_server.chat.dto.response.GetChatRoomListDto;
import com.blocker.blocker_server.chat.exception.ChatRoomNotFoundException;
import com.blocker.blocker_server.chat.exception.IsNotChatParticipantException;
import com.blocker.blocker_server.chat.mongo.ChatMessageRepository;
import com.blocker.blocker_server.chat.repository.ChatRoomRepository;
import com.blocker.blocker_server.chat.repository.ChatUserRepository;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatServiceSupport {

    private final ChatRoomRepository chatRoomRepository;
    private final BoardServiceSupport boardServiceSupport;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatUserRepository chatUserRepository;

    public void saveChatMessage(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
    }

    public ChatRoom getChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("chat room id: " + chatRoomId));
    }

    public void saveChatRoom(ChatRoom chatRoom) {
        chatRoomRepository.save(chatRoom);
    }

    public List<ChatUser> createChatUsers(ChatRoom chatRoom, User user, List<String> chatParticipants) {
        List<ChatUser> chatUsers = chatParticipants.stream()
                .map(participant -> ChatUser.create(
                        userRepository.getReferenceById(participant),
                        chatRoom
                ))
                .collect(Collectors.toList());
        chatUsers.add(ChatUser.create(user, chatRoom));

        return chatUsers;
    }

    public void saveChatUsers(List<ChatUser> chatUsers) {
        chatUserRepository.saveAll(chatUsers);
    }

    public List<ChatRoom> getChatRoomsByUser(User user) {
        return chatRoomRepository.findChatRoomsByUser(user);
    }

    public List<GetChatRoomListDto> createChatRoomListResponseDto(List<ChatRoom> chatRooms) {
        return chatRooms.stream()
                .map(chatRoom -> GetChatRoomListDto.of(chatRoom.getChatRoomID(), chatRoom.getLastChat(), chatRoom.getLastChatTime()))
                .collect(Collectors.toList());
    }

    public void checkIsChatParticipant(User user, Long chatRoomId) {
        if (!chatUserRepository.existsByUserAndChatRoom(user, chatRoomRepository.getReferenceById(chatRoomId)))
            throw new IsNotChatParticipantException("user: " + user.getEmail() +", chat room id: " + chatRoomId);
    }

    public List<ChatMessage> getChatMessagesByChatRoomId(Long chatRoomId, Pageable pageable) {
        return chatMessageRepository.findByChatRoomId(chatRoomId, pageable);
    }

    public List<GetChatMessagesResponseDto> createChatMessageResponseDto(List<ChatMessage> chatMessages) {
        return chatMessages.stream()
                .map(chatMessage -> GetChatMessagesResponseDto.of(chatMessage.getSender(), chatMessage.getContent(), chatMessage.getSendAt()))
                .collect(Collectors.toList());
    }

    public User getBoardWriter(String email, Long boardId) {
        Board board = boardServiceSupport.getBoardById(boardId);

        return board.getUser();
    }

    public Long getOneToOneChatRoomByUsers(String me, String boardWriter) {
        Long chatRoomId = chatRoomRepository.findOneToOneChatRoomByUsers(me, boardWriter);

        if(chatRoomId == null)
            chatRoomId = createOneToOneChatRoom(me, boardWriter);

        return chatRoomId;
    }

    private Long createOneToOneChatRoom(String me, String boardWriter) {

        ChatRoom chatRoom = ChatRoom.builder().createdAt(LocalDateTime.now()).build();

        chatRoomRepository.save(chatRoom);

        List<ChatUser> chatUsers = List.of(
                ChatUser.create(userRepository.getReferenceById(me), chatRoom),
                ChatUser.create(userRepository.getReferenceById(boardWriter), chatRoom)
        );

        chatUserRepository.saveAll(chatUsers);

        return chatRoom.getChatRoomID();
    }

}
