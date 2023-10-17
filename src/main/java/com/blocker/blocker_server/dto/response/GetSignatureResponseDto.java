package com.blocker.blocker_server.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetSignatureResponseDto {
    private String address;
}
