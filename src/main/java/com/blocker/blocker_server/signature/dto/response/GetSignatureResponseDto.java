package com.blocker.blocker_server.signature.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetSignatureResponseDto {
    private String address;
}
