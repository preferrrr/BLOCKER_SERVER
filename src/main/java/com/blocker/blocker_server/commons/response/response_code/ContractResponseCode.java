package com.blocker.blocker_server.commons.response.response_code;

import com.blocker.blocker_server.commons.response.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ContractResponseCode implements SuccessCode {

    SAVE_CONTRACT(HttpStatus.CREATED, "계약서 생성에 성공했습니다."),
    MODIFY_CONTRACT(HttpStatus.OK, "계약서 수정에 성공했습니다."),
    GET_CONTRACT_LIST(HttpStatus.OK, "계약서 리스트 조회에 성공했습니다."),
    GET_NOT_PROCEED_CONTRACT(HttpStatus.OK, "미체결 계약서 조회에 성공했습니다."),
    GET_PROCEED_CONTRACT(HttpStatus.OK, "진행 중 계약서 조회에 성공헀습니다."),
    GET_CONCLUDE_CONTRACT(HttpStatus.OK, "체결 계약서 조회에 성공헀습니다."),
    DELETE_CONTRACT(HttpStatus.OK, "계약서 삭제에 성공했습니다."),
    DELETE_CONTRACT_WITH_BOARD(HttpStatus.OK, "계약서와 게시글 삭제에 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
