package com.blocker.blocker_server.chat.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import com.blocker.blocker_server.commons.exception.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ChatRoomExceptionHandler {

    @ExceptionHandler(ChatRoomNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotExistBookmarkException(final ChatRoomNotFoundException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        );
    }

    @ExceptionHandler(IsNotChatParticipantException.class)
    public ResponseEntity<ExceptionResponse> handleNotExistBookmarkException(final IsNotChatParticipantException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        );
    }
}
