package com.blocker.blocker_server.contract.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ContractExceptionHandler {

    @ExceptionHandler(ContractNotFoundException.class)
    public ResponseEntity<HttpStatus> handleContractNotFoundException(final ContractNotFoundException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND); /**계약서를 찾을 수 없음*/
    }


    @ExceptionHandler(IsNotContractWriterException.class)
    public ResponseEntity<HttpStatus> handleIsNotContractWriterException(final IsNotContractWriterException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.FORBIDDEN); /** 계약서 작성자가 아님*/
    }

    @ExceptionHandler(CannotModifyContractInConcludedStateException.class)
    public ResponseEntity<HttpStatus> handleCannotModifyContractInConcludedStateException(final CannotModifyContractInConcludedStateException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); /** 체결 완료된 계약서는 수정할 수 없음.*/
    }

    @ExceptionHandler(ExistBoardsBelongingToContractException.class)
    public ResponseEntity<HttpStatus> handleExistBoardsBelongingToContractException(final ExistBoardsBelongingToContractException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); /** 해당 계약서가 포함된 게시글이 있어서 지울 수 없음.*/
    }

    @ExceptionHandler(IsNotProceedContractException.class)
    public ResponseEntity<?> handleIsNotProceedContractException(final IsNotProceedContractException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); /** 진행 중 계약서가 아님.*/
    }

    @ExceptionHandler(IsNotContractParticipantException.class)
    public ResponseEntity<?> handleIsNotParticipantException(final IsNotContractParticipantException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.FORBIDDEN); /** 계약 참가자가 아니면 볼 수 없음.*/
    }

    @ExceptionHandler(IsNotConcludeContractException.class)
    public ResponseEntity<?> handleIsNotConcludeContractException(final IsNotConcludeContractException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); /** 체결된 계약서가 아님.*/
    }

    @ExceptionHandler(CancelContractNotFoundException.class)
    public ResponseEntity<?> handleCancelContractNotFoundException(final CancelContractNotFoundException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND); /** 파기 계약서 찾을 수 없음.*/
    }

    @ExceptionHandler(IsNotCancelingCancelContract.class)
    public ResponseEntity<?> handleIsNotCancelingCancelContract(final IsNotCancelingCancelContract e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); /** 파기 진행 중 계약서가 아님.*/
    }

    @ExceptionHandler(IsNotCancelContractParticipant.class)
    public ResponseEntity<?> handleIsNotCancelContractParticipant(final IsNotCancelContractParticipant e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.FORBIDDEN); /** 파기 계약 참여자가 아님.*/
    }

    @ExceptionHandler(IsNotCanceledCancelContract.class)
    public ResponseEntity<?> handleIsNotCanceledCancelContract(final IsNotCanceledCancelContract e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); /** 파기 체결된 계약서가 아님.*/
    }
}
