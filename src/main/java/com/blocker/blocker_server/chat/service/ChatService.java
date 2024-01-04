package com.blocker.blocker_server.chat.service;

import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.board.repository.BoardRepository;
import com.blocker.blocker_server.chat.domain.ChatRoom;
import com.blocker.blocker_server.chat.domain.ChatUser;
import com.blocker.blocker_server.chat.dto.request.SendMessageRequestDto;
import com.blocker.blocker_server.chat.dto.response.GetChatMessagesResponseDto;
import com.blocker.blocker_server.chat.dto.response.GetChatRoomListDto;
import com.blocker.blocker_server.chat.domain.ChatMessage;
import com.blocker.blocker_server.chat.dto.response.GetOneToOneChatRoomResponse;
import com.blocker.blocker_server.chat.mongo.ChatMessageRepository;
import com.blocker.blocker_server.commons.exception.ForbiddenException;
import com.blocker.blocker_server.commons.exception.NotFoundException;
import com.blocker.blocker_server.commons.kafka.KafkaChatConstant;
import com.blocker.blocker_server.commons.kafka.KafkaMessage;
import com.blocker.blocker_server.commons.kafka.MessageSender;
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
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final JwtProvider jwtProvider;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatUserRepository chatUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MessageSender sender;
    private final BoardRepository boardRepository;

    @Transactional
    public void sendMessage(String token, Long chatRoomId, SendMessageRequestDto requestDto) {

        String tokenValue = token.substring(7);

        String email = jwtProvider.getEmail(tokenValue);
        String username = jwtProvider.getUsername(tokenValue);
        User user = userRepository.getReferenceById(email);

        //메세지 mongoDB에 저장
        ChatMessage chatMessage = ChatMessage.create(chatRoomId, username, email, requestDto.getContent(), LocalDateTime.now());

        chatMessageRepository.save(chatMessage);

        //채팅방의 마지막 채팅 업데이트, 이거 또한 write가 자주 일어나니까 mongoDB에 저장할까 ?
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new NotFoundException("[send message] user, chatRoomId : " + user.getEmail() + ", " + chatRoomId));
        chatRoom.updateLastChat(chatMessage.getContent(), chatMessage.getSendAt());

        //메세지의 sender는 email이 아닌 username으로 가야함.
        //jwt 안에는 email만 있었는데 email로 DB를 조회해서 매번 가져오는거보다,
        //jwt의 길이가 약간 길어지는 것을 감수하고 username을 포함시켜서 DB 조회를 하지 않도록 함.
        KafkaMessage message = KafkaMessage.create(String.valueOf(chatRoomId), username, requestDto.getContent());

        //simpMessagingTemplate.convertAndSend("/sub/" + chatRoomId, message);
        sender.send(KafkaChatConstant.KAFKA_TOPIC, message);

    }

    @Transactional
    public void createChatRoom(User user, List<String> emails) {

        ChatRoom chatRoom = ChatRoom.create(LocalDateTime.now());

        chatRoomRepository.save(chatRoom);

        List<ChatUser> chatUsers = emails.stream()
                .map(email -> ChatUser.create(userRepository.getReferenceById(email), chatRoom))
                .collect(Collectors.toList());

        chatUsers.add(ChatUser.builder().user(user).chatRoom(chatRoom).build());

        chatUserRepository.saveAll(chatUsers);

    }

    public List<GetChatRoomListDto> getChatRooms(User user) {

        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByUser(user);

        List<GetChatRoomListDto> response = chatRooms.stream()
                .map(chatRoom -> GetChatRoomListDto.of(chatRoom.getChatRoomID(), chatRoom.getLastChat(), chatRoom.getLastChatTime()))
                .collect(Collectors.toList());

        return response;
    }


    public List<GetChatMessagesResponseDto> getMessages(User user, Long chatRoomId, Pageable pageable) {

        // 자신의 채팅방 아니면 채팅 내역 조회할 수 없음.
        if (!chatUserRepository.existsByUserAndChatRoom(user, chatRoomRepository.getReferenceById(chatRoomId)))
            throw new ForbiddenException("[get messages] user, chatRoomId : " + user.getEmail() + ", " + chatRoomId);

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(chatRoomId, pageable);

        List<GetChatMessagesResponseDto> response = chatMessages.stream()
                .map(chatMessage -> GetChatMessagesResponseDto.of(chatMessage.getSender(), chatMessage.getContent(), chatMessage.getSendAt()))
                .collect(Collectors.toList());

        return response;
    }


    @Transactional
    public GetOneToOneChatRoomResponse getOneToOneChatRoomId(User user, Long boardId) {

        User boardWriter = getBoardWriter(user.getEmail(), boardId);

        Long chatRoomId = chatRoomRepository.findOneToOneChatRoomByUsers(user.getEmail(), boardWriter.getEmail());

        if (chatRoomId == null) {
            chatRoomId = createOneToOneChatRoom(user.getEmail(), boardWriter.getEmail());
        }

        return GetOneToOneChatRoomResponse.of(chatRoomId);
    }

    private User getBoardWriter(String email, Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException("[get one to one chat room] user, boardId : " + email + ", " + boardId));

        return board.getUser();
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
