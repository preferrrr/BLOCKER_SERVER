package com.blocker.blocker_server.signature.exception;

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
    public ResponseEntity<HttpStatus> handleAlreadyHaveSignatureException(final AlreadyHaveSignatureException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.CONFLICT); /** 이미 전자서명 가지고 있음 */
    }

    @ExceptionHandler(SignatureNotFoundException.class)
    public ResponseEntity<HttpStatus> handleSignatureNotFoundException(final SignatureNotFoundException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND); /** 전자서명 없음 */
    }
}
