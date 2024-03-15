package com.blocker.blocker_server.board.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import com.blocker.blocker_server.commons.exception.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class BoardExceptionHandler {

    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleBoardNotFoundException(final BoardNotFoundException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("[board not found exception] {}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus());
    }

    @ExceptionHandler(UnauthorizedDeleteBoardException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedDeleteException(final UnauthorizedDeleteBoardException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("[unauthorized delete exception] {}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus());
    }

    @ExceptionHandler(UnauthorizedModifyBoardException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedModifyException(final UnauthorizedModifyBoardException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("[unauthorized modify exception] {}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus());
    }
}
