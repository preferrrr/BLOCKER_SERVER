package com.blocker.blocker_server.sign.exception;

import com.blocker.blocker_server.contract.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AgreementSignExceptionHandler {

    @ExceptionHandler(EmptyParticipantException.class)
    public ResponseEntity<HttpStatus> handleEmptyParticipantException(final EmptyParticipantException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); /** 참여자가 없음 */
    }

    @ExceptionHandler(IsAlreadySignedException.class)
    public ResponseEntity<HttpStatus> handleIsAlreadySignedException(final IsAlreadySignedException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.CONFLICT); /** 이미 서명했음 */
    }
}
