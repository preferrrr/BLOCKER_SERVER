package com.blocker.blocker_server.sign.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class SignExceptionHandler {

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

    @ExceptionHandler(IsNotConcludeContractForCancelException.class)
    public ResponseEntity<HttpStatus> handleIsNotConcludeContractForCancelException(final IsNotConcludeContractForCancelException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); /** 체결된 계약서가 아님 */
    }

    @ExceptionHandler(IsNotContractParticipantForCancelException.class)
    public ResponseEntity<HttpStatus> handleIsNotContractParticipantForCancelException(final IsNotContractParticipantForCancelException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.FORBIDDEN); /** 계약 참여자가 아님 */
    }

    @ExceptionHandler(IsAlreadyCancelingException.class)
    public ResponseEntity<HttpStatus> handleIsAlreadyCancelingException(final IsAlreadyCancelingException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.CONFLICT); /** 이미 파기 진행 중임 */
    }

    @ExceptionHandler(IsNotCancelContractParticipantException.class)
    public ResponseEntity<HttpStatus> handleIsNotCancelContractParticipantException(final IsNotCancelContractParticipantException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.FORBIDDEN); /** 파기 계약 참여자가 아님 */
    }

    @ExceptionHandler(IsAlreadyCancelSignException.class)
    public ResponseEntity<HttpStatus> handleIsAlreadyCancelSignException(final IsAlreadyCancelSignException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.CONFLICT); /** 이미 파기 계약 서명함 */
    }
}
