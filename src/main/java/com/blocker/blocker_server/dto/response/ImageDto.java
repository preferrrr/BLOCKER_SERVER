package com.blocker.blocker_server.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class ImageDto {
    private Long imageId;
    private String imageAddress;
}
