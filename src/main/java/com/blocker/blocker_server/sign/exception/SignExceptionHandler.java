package com.blocker.blocker_server.sign.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import com.blocker.blocker_server.commons.exception.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class SignExceptionHandler {

    @ExceptionHandler(EmptyParticipantException.class)
    public ResponseEntity<ExceptionResponse> handleEmptyParticipantException(final EmptyParticipantException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /** 참여자가 없음 */
    }

    @ExceptionHandler(IsAlreadySignedException.class)
    public ResponseEntity<ExceptionResponse> handleIsAlreadySignedException(final IsAlreadySignedException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /** 이미 서명했음 */
    }

    @ExceptionHandler(IsNotConcludeContractForCancelException.class)
    public ResponseEntity<ExceptionResponse> handleIsNotConcludeContractForCancelException(final IsNotConcludeContractForCancelException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /** 체결된 계약서가 아님 */
    }

    @ExceptionHandler(IsNotContractParticipantForCancelException.class)
    public ResponseEntity<ExceptionResponse> handleIsNotContractParticipantForCancelException(final IsNotContractParticipantForCancelException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /** 계약 참여자가 아님 */
    }

    @ExceptionHandler(IsAlreadyCancelingException.class)
    public ResponseEntity<ExceptionResponse> handleIsAlreadyCancelingException(final IsAlreadyCancelingException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /** 이미 파기 진행 중임 */
    }

    @ExceptionHandler(IsNotCancelContractParticipantException.class)
    public ResponseEntity<ExceptionResponse> handleIsNotCancelContractParticipantException(final IsNotCancelContractParticipantException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /** 파기 계약 참여자가 아님 */
    }

    @ExceptionHandler(IsAlreadyCancelSignException.class)
    public ResponseEntity<ExceptionResponse> handleIsAlreadyCancelSignException(final IsAlreadyCancelSignException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /** 이미 파기 계약 서명함 */
    }
}
