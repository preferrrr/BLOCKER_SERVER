package com.blocker.blocker_server.commons.response.response_code;

import com.blocker.blocker_server.commons.response.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatResponseCode implements SuccessCode {

    POST_CHAT_ROOM(HttpStatus.CREATED, "채팅방 생성에 성공했습니다."),
    GET_CHAT_ROOM_LIST(HttpStatus.OK, "채팅방 리스트 조회에 성공했습니다."),
    GET_CHAT_MESSAGES(HttpStatus.OK, "채팅 메시지 조회에 성공헀습니다."),
    GET_CHAT_ROOM_ID(HttpStatus.OK, "1:1 채팅방 조회에 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
