package com.blocker.blocker_server.chat.controller;

import com.blocker.blocker_server.chat.dto.response.GetChatMessagesResponseDto;
import com.blocker.blocker_server.chat.dto.response.GetOneToOneChatRoomResponse;
import com.blocker.blocker_server.chat.service.ChatService;
import com.blocker.blocker_server.chat.dto.request.SendMessageRequestDto;
import com.blocker.blocker_server.chat.dto.request.CreateChatRoomRequestDto;
import com.blocker.blocker_server.chat.dto.response.GetChatRoomListDto;
import com.blocker.blocker_server.commons.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.blocker.blocker_server.commons.response.response_code.ChatResponseCode.*;


@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * 메시지 전송
     */
    @MessageMapping("/message/{chatRoomId}")
    public void sendMessage(@Header("Authorization") String token, @DestinationVariable("chatRoomId") Long roomId, SendMessageRequestDto chatMessage) {

        chatService.sendMessage(token, roomId, chatMessage);

    }

    /**
     * 채팅방 생성
     * /chatrooms
     */
    @PostMapping("/chatrooms")
    public ApiResponse createRoom(@RequestBody @Valid CreateChatRoomRequestDto createChatRoomRequestDto) {

        chatService.createChatRoom(createChatRoomRequestDto.getChatUsers());

        return ApiResponse.of(POST_CHAT_ROOM);
    }

    /**
     * 채팅방 리스트 조회
     */
    @GetMapping("/chatrooms")
    public ApiResponse<GetChatRoomListDto> getChatRooms() {

        return ApiResponse.of(
                chatService.getChatRooms(),
                GET_CHAT_ROOM_LIST
        );
    }

    /**
     * 채팅방 메세지 조회
     */
    @GetMapping("/chatrooms/{chatRoomId}")
    public ApiResponse<GetChatMessagesResponseDto> getMessages(@PathVariable("chatRoomId") Long chatRoomId,
                                                               @PageableDefault(size = 20, sort = "sendAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return ApiResponse.of(
                chatService.getMessages(chatRoomId, pageable),
                GET_CHAT_MESSAGES
        );
    }

    /**
     * 게시글에서 작성자와 1:1 채팅
     */
    @GetMapping("/chatrooms/boards/{boardId}")
    public ApiResponse<GetOneToOneChatRoomResponse> getOneToOneChatRoom(@PathVariable("boardId") Long boardId) {

        return ApiResponse.of(
                chatService.getOneToOneChatRoomId(boardId),
                GET_CHAT_ROOM_ID
        );
    }


}

