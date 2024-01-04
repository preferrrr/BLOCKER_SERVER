package com.blocker.blocker_server.signature.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetSignatureResponseDto {
    private String address;

    @Builder
    private GetSignatureResponseDto(String address) {
        this.address = address;
    }

    public static GetSignatureResponseDto of(String signatureAddress) {
        return GetSignatureResponseDto.builder()
                .address(signatureAddress)
                .build();

    }
}
