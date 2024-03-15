package com.blocker.blocker_server.contract.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import com.blocker.blocker_server.commons.exception.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ContractExceptionHandler {

    @ExceptionHandler(ContractNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleContractNotFoundException(final ContractNotFoundException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /**계약서를 찾을 수 없음*/
    }


    @ExceptionHandler(IsNotContractWriterException.class)
    public ResponseEntity<ExceptionResponse> handleIsNotContractWriterException(final IsNotContractWriterException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /** 계약서 작성자가 아님*/
    }

    @ExceptionHandler(CannotModifyContractInConcludedStateException.class)
    public ResponseEntity<ExceptionResponse> handleCannotModifyContractInConcludedStateException(final CannotModifyContractInConcludedStateException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /** 체결 완료된 계약서는 수정할 수 없음.*/
    }

    @ExceptionHandler(ExistBoardsBelongingToContractException.class)
    public ResponseEntity<ExceptionResponse> handleExistBoardsBelongingToContractException(final ExistBoardsBelongingToContractException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /** 해당 계약서가 포함된 게시글이 있어서 지울 수 없음.*/
    }

    @ExceptionHandler(IsNotProceedContractException.class)
    public ResponseEntity<ExceptionResponse> handleIsNotProceedContractException(final IsNotProceedContractException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /** 진행 중 계약서가 아님.*/
    }

    @ExceptionHandler(IsNotContractParticipantException.class)
    public ResponseEntity<ExceptionResponse> handleIsNotParticipantException(final IsNotContractParticipantException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /** 계약 참가자가 아니면 볼 수 없음.*/
    }

    @ExceptionHandler(IsNotConcludeContractException.class)
    public ResponseEntity<ExceptionResponse> handleIsNotConcludeContractException(final IsNotConcludeContractException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /** 체결된 계약서가 아님.*/
    }

    @ExceptionHandler(CancelContractNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleCancelContractNotFoundException(final CancelContractNotFoundException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /** 파기 계약서 찾을 수 없음.*/
    }

    @ExceptionHandler(IsNotCancelingCancelContract.class)
    public ResponseEntity<ExceptionResponse> handleIsNotCancelingCancelContract(final IsNotCancelingCancelContract e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /** 파기 진행 중 계약서가 아님.*/
    }

    @ExceptionHandler(IsNotCancelContractParticipant.class)
    public ResponseEntity<ExceptionResponse> handleIsNotCancelContractParticipant(final IsNotCancelContractParticipant e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /** 파기 계약 참여자가 아님.*/
    }

    @ExceptionHandler(IsNotCanceledCancelContract.class)
    public ResponseEntity<ExceptionResponse> handleIsNotCanceledCancelContract(final IsNotCanceledCancelContract e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        ); /** 파기 체결된 계약서가 아님.*/
    }

    @ExceptionHandler(InvalidContractStateException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidContractState(final InvalidContractStateException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        );
    }
}
