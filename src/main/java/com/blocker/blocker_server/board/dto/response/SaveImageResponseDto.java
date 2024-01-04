package com.blocker.blocker_server.board.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SaveImageResponseDto {
    private String address;

    @Builder
    private SaveImageResponseDto(String address) {
        this.address = address;
    }

    public static SaveImageResponseDto of(String address) {
        return SaveImageResponseDto.builder()
                .address(address)
                .build();
    }
}
