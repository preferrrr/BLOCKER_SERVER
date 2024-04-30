package com.blocker.blocker_server.board.dto.response;

import com.blocker.blocker_server.contract.domain.ContractState;

import java.time.LocalDateTime;

public interface GetBoardListResponseDtoInterface {
    Long getBoardId();
    String getTitle();
    String getName();
    String getContent();
    String getRepresentImage();
    int getView();
    int getBookmarkCount();
    ContractState getContractState();
    LocalDateTime getCreatedAt();
    LocalDateTime getModifiedAt();

}
