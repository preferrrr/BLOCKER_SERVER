package com.blocker.blocker_server.sign.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum SignExceptionCode implements ExceptionCode {

    EMPTY_PARTICIPANT(HttpStatus.BAD_REQUEST, "참여자가 없습니다."),
    IS_ALREADY_CANCELING(HttpStatus.CONFLICT, "이미 파기 진행 중입니다."),
    IS_ALREADY_CANCEL_SIGN(HttpStatus.CONFLICT, "이미 파기 계약에 서명했습니다."),
    IS_ALREADY_SIGNED(HttpStatus.CONFLICT, "이미 계약에 서명했습니다."),
    IS_NOT_CANCEL_CONTRACT_PARTICIPANT(HttpStatus.FORBIDDEN, "파기 계약 참여자가 아닙니다."),
    IS_NOT_CONCLUDE_CONTRACT_FOR_CANCEL(HttpStatus.BAD_REQUEST, "체결된 계약서가 아닙니다."),
    IS_NOT_CONTRACT_PARTICIPANT_FOR_CANCEL(HttpStatus.FORBIDDEN, "계약 참여자가 아닙니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
