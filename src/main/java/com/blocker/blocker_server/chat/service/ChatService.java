package com.blocker.blocker_server.chat.service;

import com.blocker.blocker_server.chat.domain.ChatRoom;
import com.blocker.blocker_server.chat.domain.ChatUser;
import com.blocker.blocker_server.chat.dto.request.SendMessageRequestDto;
import com.blocker.blocker_server.chat.dto.response.GetChatMessagesResponseDto;
import com.blocker.blocker_server.chat.dto.response.GetChatRoomListDto;
import com.blocker.blocker_server.chat.domain.ChatMessage;
import com.blocker.blocker_server.chat.dto.response.GetOneToOneChatRoomResponse;
import com.blocker.blocker_server.commons.kafka.KafkaChatConstant;
import com.blocker.blocker_server.commons.kafka.KafkaMessage;
import com.blocker.blocker_server.commons.kafka.MessageSender;
import com.blocker.blocker_server.commons.utils.CurrentUserGetter;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.commons.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatServiceSupport chatServiceSupport;
    private final JwtProvider jwtProvider;
    private final MessageSender sender;
    private final CurrentUserGetter currentUserGetter;

    @Transactional
    public void sendMessage(String token, Long chatRoomId, SendMessageRequestDto requestDto) {

        //토큰에서 email, username 꺼냄
        String tokenValue = token.substring(7);
        String email = jwtProvider.getEmail(tokenValue);
        String username = jwtProvider.getUsername(tokenValue);

        //메세지 mongoDB에 저장
        ChatMessage chatMessage = ChatMessage.create(chatRoomId, username, email, requestDto.getContent(), LocalDateTime.now());
        chatServiceSupport.saveChatMessage(chatMessage);


        //채팅방의 마지막 채팅 업데이트, 이거 또한 write가 자주 일어나니까 mongoDB에 저장할까 ?
        ChatRoom chatRoom = chatServiceSupport.getChatRoomById(chatRoomId);
        chatRoom.updateLastChat(chatMessage.getContent(), chatMessage.getSendAt());

        //메세지의 sender는 email이 아닌 username으로 가야함.
        //jwt 안에는 email만 있었는데 email로 DB를 조회해서 매번 가져오는거보다,
        //jwt의 길이가 약간 길어지는 것을 감수하고 username을 포함시켜서 DB 조회를 하지 않도록 함.
        KafkaMessage message = KafkaMessage.create(String.valueOf(chatRoomId), username, requestDto.getContent());

        //카프카로 메세지 전송
        sender.send(KafkaChatConstant.KAFKA_TOPIC, message);

    }

    @Transactional
    public void createChatRoom(List<String> chatParticipants) {

        User user = currentUserGetter.getCurrentUser();

        //새로운 채팅방
        ChatRoom chatRoom = ChatRoom.create(LocalDateTime.now());

        //채팅방 저장
        chatServiceSupport.saveChatRoom(chatRoom);

        //채팅 참여자들
        List<ChatUser> chatUsers = chatServiceSupport.createChatUsers(chatRoom, user, chatParticipants);

        //채팅 참여자들 저장
        chatServiceSupport.saveChatUsers(chatUsers);

    }

    public List<GetChatRoomListDto> getChatRooms() {

        User user = currentUserGetter.getCurrentUser();

        //나의 채팅방들 조회
        List<ChatRoom> chatRooms = chatServiceSupport.getChatRoomsByUser(user);

        //dto로 변환해서 반환
        return chatServiceSupport.createChatRoomListResponseDto(chatRooms);
    }


    public List<GetChatMessagesResponseDto> getMessages(Long chatRoomId, Pageable pageable) {

        User user = currentUserGetter.getCurrentUser();

        //자신의 채팅방인지 검사
        chatServiceSupport.checkIsChatParticipant(user, chatRoomId);

        //메세지 조회
        List<ChatMessage> chatMessages = chatServiceSupport.getChatMessagesByChatRoomId(chatRoomId, pageable);

        //dto로 변환하고 반환
        return chatServiceSupport.createChatMessageResponseDto(chatMessages);
    }


    @Transactional
    public GetOneToOneChatRoomResponse getOneToOneChatRoomId(Long boardId) {

        User user = currentUserGetter.getCurrentUser();

        //게시글 작성자
        User boardWriter = chatServiceSupport.getBoardWriter(boardId);

        //채팅방 인덱스
        Long chatRoomId = chatServiceSupport.getOneToOneChatRoomByUsers(user.getEmail(), boardWriter.getEmail());

        return GetOneToOneChatRoomResponse.of(chatRoomId);
    }


}
