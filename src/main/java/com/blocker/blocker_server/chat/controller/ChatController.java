package com.blocker.blocker_server.chat.controller;

import com.blocker.blocker_server.chat.dto.response.GetChatMessagesResponseDto;
import com.blocker.blocker_server.chat.dto.response.GetOneToOneChatRoomResponse;
import com.blocker.blocker_server.chat.service.ChatService;
import com.blocker.blocker_server.chat.dto.request.SendMessageRequestDto;
import com.blocker.blocker_server.chat.dto.request.CreateChatRoomRequestDto;
import com.blocker.blocker_server.chat.dto.response.GetChatRoomListDto;
import com.blocker.blocker_server.commons.response.BaseResponse;
import com.blocker.blocker_server.commons.response.ListResponse;
import com.blocker.blocker_server.commons.response.SingleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<BaseResponse> createRoom(@RequestBody CreateChatRoomRequestDto createChatRoomRequestDto) {

        chatService.createChatRoom(createChatRoomRequestDto.getChatUsers());

        return ResponseEntity.ok(BaseResponse.ok());
    }

    /**
     * 채팅방 리스트 조회
     */
    @GetMapping("/chatrooms")
    public ResponseEntity<ListResponse<GetChatRoomListDto>> getChatRooms() {

        return ResponseEntity.ok(
                ListResponse.ok(chatService.getChatRooms())
        );
    }

    /**
     * 채팅방 메세지 조회
     */
    @GetMapping("/chatrooms/{chatRoomId}")
    public ResponseEntity<ListResponse<GetChatMessagesResponseDto>> getMessages(@PathVariable("chatRoomId") Long chatRoomId,
                                                                                @PageableDefault(size = 20, sort = "sendAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(
                ListResponse.ok(chatService.getMessages(chatRoomId, pageable))
        );
    }

    /**
     * 게시글에서 작성자와 1:1 채팅
     */
    @GetMapping("/chatrooms/boards/{boardId}")
    public ResponseEntity<SingleResponse<GetOneToOneChatRoomResponse>> getOneToOneChatRoom(@PathVariable("boardId") Long boardId) {

        return ResponseEntity.ok(
                SingleResponse.ok(chatService.getOneToOneChatRoomId(boardId))
        );
    }


}

