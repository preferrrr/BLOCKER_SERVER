package com.blocker.blocker_server.chat.controller;

import com.blocker.blocker_server.chat.dto.request.CreateChatRoomRequestDto;
import com.blocker.blocker_server.chat.dto.request.SendMessageRequestDto;
import com.blocker.blocker_server.chat.dto.response.GetChatMessagesResponseDto;
import com.blocker.blocker_server.chat.dto.response.GetChatRoomListDto;
import com.blocker.blocker_server.chat.dto.response.GetOneToOneChatRoomResponse;
import com.blocker.blocker_server.chat.service.ChatService;
import com.blocker.blocker_server.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ChatController.class)
@WithMockUser(roles = "USER")
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChatService chatService;


    @DisplayName("채팅방을 생성한다.")
    @Test
    void createRoom() throws Exception {

        /** given */

        CreateChatRoomRequestDto request = CreateChatRoomRequestDto.builder()
                .chatUsers(List.of("testEmail", "testEmail2"))
                .build();

        willDoNothing().given(chatService).createChatRoom(any(), anyList());

        /** when */

        /** then */

        mockMvc.perform(post("/chatrooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated());

        verify(chatService, times(1)).createChatRoom(any(), anyList());

    }

    @DisplayName("채팅방을 생성할 때, 채팅 참여자 리스트가 비어있으면 204을 반환한다.")
    @Test
    void createRoomNoContent() throws Exception {

        /** given */

        CreateChatRoomRequestDto request = CreateChatRoomRequestDto.builder()
                .chatUsers(List.of())
                .build();

        /** when */

        /** then */

        mockMvc.perform(post("/chatrooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }


    @DisplayName("채팅방 리스트를 조회한다.")
    @Test
    void getChatRooms() throws Exception {

        /** given */
        List<GetChatRoomListDto> response = List.of();

        given(chatService.getChatRooms(any())).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/chatrooms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

    }

    @DisplayName("채팅방 메세지를 조회한다.")
    @Test
    void getMessages() throws Exception {

        /** given */
        List<GetChatMessagesResponseDto> response = List.of();

        given(chatService.getMessages(any(), anyLong(), any(Pageable.class))).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/chatrooms/{chatRoomId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(chatService, times(1)).getMessages(any(), anyLong(), any(Pageable.class));
    }

    @DisplayName("게시글 작성자와의 1:1 채팅방 인덱스를 반환한다.")
    @Test
    void getOneToOneChatRoom() throws Exception {

        /** given */

        GetOneToOneChatRoomResponse response = GetOneToOneChatRoomResponse.builder()
                .chatRoomId(1l)
                .build();
        given(chatService.getOneToOneChatRoomId(any(), anyLong())).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/chatrooms/boards/{boardId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatRoomId").exists());

        verify(chatService, times(1)).getOneToOneChatRoomId(any(), anyLong());

    }

}