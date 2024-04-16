package com.blocker.blocker_server.chat.controller;

import com.blocker.blocker_server.ControllerTestSupport;
import com.blocker.blocker_server.chat.dto.request.CreateChatRoomRequestDto;
import com.blocker.blocker_server.chat.dto.response.GetChatRoomListDto;
import com.blocker.blocker_server.chat.dto.response.GetOneToOneChatRoomResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(roles = "USER")
class ChatControllerTest extends ControllerTestSupport {

    @DisplayName("채팅방을 생성한다.")
    @Test
    void createRoom() throws Exception {

        /** given */

        CreateChatRoomRequestDto request = CreateChatRoomRequestDto.builder()
                .chatUsers(List.of("testEmail", "testEmail2"))
                .build();

        willDoNothing().given(chatService).createChatRoom(any());

        /** when */

        /** then */

        mockMvc.perform(post("/chatrooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"));

        verify(chatService, times(1)).createChatRoom(any());

    }

    @DisplayName("채팅방을 생성할 때, 채팅 참여자 리스트가 비어있으면 400을 반환한다.")
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("채팅 상대방은 1명 이상이어야 합니다."));

        verify(chatService, times(0)).createChatRoom(any());

    }


    @DisplayName("채팅방 리스트를 조회한다.")
    @Test
    void getChatRooms() throws Exception {

        /** given */
        List<GetChatRoomListDto> response = List.of();

        given(chatService.getChatRooms()).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/chatrooms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").isArray());

        verify(chatService, times(1)).getChatRooms();

    }

    @DisplayName("채팅방 메세지를 조회한다.")
    @Test
    void getMessages() throws Exception {

        /** given */

        given(chatService.getMessages(anyLong(), any(Pageable.class))).willReturn(List.of());

        /** when */

        /** then */

        mockMvc.perform(get("/chatrooms/{chatRoomId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").isArray());

        verify(chatService, times(1)).getMessages(anyLong(), any(Pageable.class));
    }

    @DisplayName("게시글 작성자와의 1:1 채팅방 인덱스를 반환한다.")
    @Test
    void getOneToOneChatRoom() throws Exception {

        /** given */

        GetOneToOneChatRoomResponse response = GetOneToOneChatRoomResponse.builder()
                .chatRoomId(1l)
                .build();
        given(chatService.getOneToOneChatRoomId(anyLong())).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/chatrooms/boards/{boardId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(chatService, times(1)).getOneToOneChatRoomId(anyLong());

    }

}