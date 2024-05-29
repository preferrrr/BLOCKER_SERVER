package com.blocker.blocker_server.Image.controller;

import com.blocker.blocker_server.Image.service.ImageService;
import com.blocker.blocker_server.Image.dto.response.SaveImageResponseDto;
import com.blocker.blocker_server.commons.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.blocker.blocker_server.commons.response.response_code.ImageResponseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("")
    public ApiResponse<SaveImageResponseDto> s3SaveImage(@RequestPart("image") MultipartFile image) throws IOException {

        return ApiResponse.of(
                imageService.s3SaveImage(image),
                POST_IMAGE
        );
    }

}
