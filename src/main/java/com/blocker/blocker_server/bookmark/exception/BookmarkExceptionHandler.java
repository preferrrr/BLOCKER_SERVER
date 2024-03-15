package com.blocker.blocker_server.bookmark.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import com.blocker.blocker_server.commons.exception.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class BookmarkExceptionHandler {

    @ExceptionHandler(IsAlreadyBookmarkedException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateBookmarkException(final IsAlreadyBookmarkedException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        );
    }

    @ExceptionHandler(IsNotBookmarkedException.class)
    public ResponseEntity<ExceptionResponse> handleNotExistBookmarkException(final IsNotBookmarkedException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        );
    }
}
