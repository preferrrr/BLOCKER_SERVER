package com.blocker.blocker_server.Image.dto.response;

import com.blocker.blocker_server.Image.domain.Image;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ImageDto {
    private Long imageId;
    private String imageAddress;

    @Builder
    private ImageDto(Long imageId, String imageAddress) {
        this.imageId = imageId;
        this.imageAddress = imageAddress;
    }

    public static ImageDto of(Image image) {
        return ImageDto.builder()
                .imageId(image.getImageId())
                .imageAddress(image.getImageAddress())
                .build();
    }
}
