package com.blocker.blocker_server.signature.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import com.blocker.blocker_server.commons.exception.ExceptionResponse;
import com.blocker.blocker_server.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class SignatureExceptionHandler {

    @ExceptionHandler(AlreadyHaveSignatureException.class)
    public ResponseEntity<ExceptionResponse> handleAlreadyHaveSignatureException(final AlreadyHaveSignatureException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /** 이미 전자서명 가지고 있음 */
    }

    @ExceptionHandler(SignatureNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleSignatureNotFoundException(final SignatureNotFoundException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /** 전자서명 없음 */
    }
}
