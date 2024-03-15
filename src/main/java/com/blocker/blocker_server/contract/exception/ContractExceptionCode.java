package com.blocker.blocker_server.contract.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ContractExceptionCode implements ExceptionCode {

    CONTRACT_NOT_FOUND(HttpStatus.NOT_FOUND, "계약서를 찾을 수 없습니다."),
    CANCEL_CONTRACT_NOT_FOUND(HttpStatus.NOT_FOUND, "파기 계약서를 찾을 수 없습니다."),
    CANNOT_MODIFY_CONTRACT_IN_CONCLUDED(HttpStatus.BAD_REQUEST, "체결된 계약서는 수정할 수 없습니다."),
    EXISTS_BOARD_BELONGING_TO_CONTRACT(HttpStatus.BAD_REQUEST, "계약서가 포함된 게시글이 있습니다."),
    IS_NOT_CANCEL_CONTRACT_PARTICIPANT(HttpStatus.BAD_REQUEST, "파기 계약 참여자가 아닙니다."),
    IS_NOT_CANCELED_CANCEL_CONTRACT(HttpStatus.BAD_REQUEST, "체결된 파기 계약서가 아닙니다."),
    IS_NOT_CANCELING_CANCEL_CONTRACT(HttpStatus.BAD_REQUEST, "파기 진행 중 계약서가 아닙니다."),
    IS_NOT_CONCLUDE_CONTRACT(HttpStatus.BAD_REQUEST, "체결된 계약서가 아닙니다."),
    IS_NOT_CONTRACT_PARTICIPANT(HttpStatus.BAD_REQUEST, "계약 참여자가 아닙니다."),
    IS_NOT_CONTRACT_WRITER(HttpStatus.BAD_REQUEST, "계약서 작성자가 아닙니다."),
    IS_NOT_NOTPROCEED_CONTRACT(HttpStatus.BAD_REQUEST, "미체결 계약서가 아닙니다."),
    IS_NOT_PROCEED_CONTRACT(HttpStatus.BAD_REQUEST, "진행 중 계약서가 아닙니다."),
    INVALID_CONTRACT_STATE(HttpStatus.BAD_REQUEST, "잘못된 계약서 상태입니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
