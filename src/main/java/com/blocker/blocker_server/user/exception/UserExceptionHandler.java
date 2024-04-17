package com.blocker.blocker_server.user.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import com.blocker.blocker_server.commons.exception.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class UserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(final UserNotFoundException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /** 유저 없음 */
    }


    @ExceptionHandler({InvalidRefreshTokenException.class})
    public ResponseEntity<ExceptionResponse> handleInvalidRefreshTokenException(final InvalidRefreshTokenException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /**401, 유효하지 않은 리프레스 토큰.*/
    }

    @ExceptionHandler({EmptyRefreshTokenException.class})
    public ResponseEntity<ExceptionResponse> handleEmptyRefreshTokenException(final EmptyRefreshTokenException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /**401, 리프레시 토큰이 null.*/
    }
}
