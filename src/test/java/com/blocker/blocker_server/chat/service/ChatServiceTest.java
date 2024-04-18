package com.blocker.blocker_server.chat.service;


import com.blocker.blocker_server.chat.domain.ChatMessage;
import com.blocker.blocker_server.chat.domain.ChatRoom;
import com.blocker.blocker_server.chat.dto.request.SendMessageRequestDto;
import com.blocker.blocker_server.chat.dto.response.GetChatMessagesResponseDto;
import com.blocker.blocker_server.chat.dto.response.GetChatRoomListDto;
import com.blocker.blocker_server.chat.dto.response.GetOneToOneChatRoomResponse;
import com.blocker.blocker_server.commons.jwt.JwtProvider;
import com.blocker.blocker_server.commons.kafka.KafkaMessage;
import com.blocker.blocker_server.commons.kafka.MessageSender;
import com.blocker.blocker_server.commons.utils.CurrentUserGetter;
import com.blocker.blocker_server.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @InjectMocks
    private ChatService chatService;
    @Mock
    private ChatServiceSupport chatServiceSupport;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private MessageSender sender;
    @Mock
    private CurrentUserGetter currentUserGetter;

    private User user;
    private User user2;
    private ChatRoom chatRoom;

    @BeforeEach
    void setUp() {
        user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        chatRoom = ChatRoom.create(LocalDateTime.of(2024, 1, 1, 12, 0));
    }

    @DisplayName("메세지를 보내는 요청을 받으면, 메세지를 mongoDB에 저장하고, 채팅방의 마지막 채팅기록이 수정되며 카프카로 메세지를 보낸다.")
    @Test
    void sendMessage() {

        /** given */
        given(jwtProvider.getEmail(anyString())).willReturn("testEmail");
        given(jwtProvider.getUsername(anyString())).willReturn("testName");
        willDoNothing().given(chatServiceSupport).saveChatMessage(any(ChatMessage.class));
        given(chatServiceSupport.getChatRoomById(anyLong())).willReturn(chatRoom);
        willDoNothing().given(sender).send(anyString(), anyString(), any(KafkaMessage.class));

        SendMessageRequestDto request = SendMessageRequestDto.builder().content("testContent").build();

        /** when */

        chatService.sendMessage("testToken", 1l, request);

        /** then */

        verify(jwtProvider, times(1)).getEmail(anyString());
        verify(jwtProvider, times(1)).getUsername(anyString());
        verify(chatServiceSupport, times(1)).saveChatMessage(any(ChatMessage.class));
        verify(chatServiceSupport, times(1)).getChatRoomById(anyLong());
        verify(sender, times(1)).send(anyString(), anyString(), any(KafkaMessage.class));

        assertThat(chatRoom.getLastChat()).isEqualTo("testContent");
    }

    @DisplayName("채팅방을 생성한다.")
    @Test
    void createChatRoom() {

        /** given */

        given(currentUserGetter.getCurrentUser()).willReturn(user);
        willDoNothing().given(chatServiceSupport).saveChatRoom(any(ChatRoom.class));
        given(chatServiceSupport.createChatUsers(any(ChatRoom.class), any(User.class), anyList())).willReturn(mock(List.class));
        willDoNothing().given(chatServiceSupport).saveChatUsers(anyList());

        /** when */

        chatService.createChatRoom(List.of("testEmail2", "testEmail3"));

        /** then */

        verify(chatServiceSupport, times(1)).saveChatRoom(any(ChatRoom.class));
        verify(chatServiceSupport, times(1)).createChatUsers(any(ChatRoom.class), any(User.class), anyList());
        verify(chatServiceSupport, times(1)).saveChatUsers(anyList());
    }

    @DisplayName("나의 채팅방 리스트를 조회한다.")
    @Test
    void getChatRooms() {

        /** given */

        given(currentUserGetter.getCurrentUser()).willReturn(user);
        given(chatServiceSupport.getChatRoomsByUser(any(User.class))).willReturn(mock(List.class));
        given(chatServiceSupport.createChatRoomListResponseDto(anyList())).willReturn(mock(List.class));

        /** when */

        List<GetChatRoomListDto> result = chatService.getChatRooms();

        /** then */

        verify(chatServiceSupport, times(1)).getChatRoomsByUser(any(User.class));
        verify(chatServiceSupport, times(1)).createChatRoomListResponseDto(anyList());

    }

    @DisplayName("채팅방의 메시지들을 조회한다.")
    @Test
    void getMessages() {

        /** given */

        given(currentUserGetter.getCurrentUser()).willReturn(user);
        willDoNothing().given(chatServiceSupport).checkIsChatParticipant(any(User.class), anyLong());
        given(chatServiceSupport.getChatMessagesByChatRoomId(anyLong(), any(Pageable.class))).willReturn(mock(List.class));
        given(chatServiceSupport.createChatMessageResponseDto(anyList())).willReturn(mock(List.class));

        /** when */

        List<GetChatMessagesResponseDto> result = chatService.getMessages(1l, PageRequest.of(0, 20));

        /** then */

        verify(chatServiceSupport, times(1)).checkIsChatParticipant(any(User.class), anyLong());
        verify(chatServiceSupport, times(1)).getChatMessagesByChatRoomId(anyLong(), any(Pageable.class));
        verify(chatServiceSupport, times(1)).createChatMessageResponseDto(anyList());

    }

    @DisplayName("게시글 작성자와의 1:1 채팅방 인덱스를 찾아서 반환한다.")
    @Test
    void getOneToOneChatRoomId() {

        /** given */

        given(currentUserGetter.getCurrentUser()).willReturn(user);
        given(chatServiceSupport.getBoardWriter(anyLong())).willReturn(user);
        given(chatServiceSupport.getOneToOneChatRoomByUsers(anyString(), anyString())).willReturn(1l);

        /** when */

        GetOneToOneChatRoomResponse result = chatService.getOneToOneChatRoomId(1l);

        /** then */

        verify(chatServiceSupport, times(1)).getBoardWriter(anyLong());
        verify(chatServiceSupport, times(1)).getOneToOneChatRoomByUsers(anyString(), anyString());
    }


}