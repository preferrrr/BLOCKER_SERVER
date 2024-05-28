package com.blocker.blocker_server.commons.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CancelContractResponseCode {

    GET_CANCEL_CONTRACT_LIST(HttpStatus.OK, "파기 계약서 리스트 조회에 성공했습니다."),
    GET_CANCELING_CONTRACT(HttpStatus.OK, "파기 중 계약서 조회에 성공했습니다."),
    GET_CANCELED_CONTRACT(HttpStatus.OK, "파기된 계약서 조회에 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
