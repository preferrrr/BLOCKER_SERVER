package com.blocker.blocker_server.chat.exception;

import com.blocker.blocker_server.contract.exception.ContractNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ChatRoomExceptionHandler {

    @ExceptionHandler(ChatRoomNotFoundException.class)
    public ResponseEntity<HttpStatus> handleChatRoomNotFoundException(final ChatRoomNotFoundException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND); /** 채팅방 찾을 수 없음*/
    }

    @ExceptionHandler(IsNotChatParticipantException.class)
    public ResponseEntity<HttpStatus> handleIsNotChatParticipantException(final IsNotChatParticipantException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.FORBIDDEN); /** 채팅방 참여자가 아님 */
    }
}
