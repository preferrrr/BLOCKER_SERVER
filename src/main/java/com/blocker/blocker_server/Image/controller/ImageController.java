package com.blocker.blocker_server.Image.controller;

import com.blocker.blocker_server.Image.service.ImageService;
import com.blocker.blocker_server.Image.dto.response.SaveImageResponseDto;
import com.blocker.blocker_server.commons.response.SingleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("")
    public ResponseEntity<SingleResponse<SaveImageResponseDto>> s3SaveImage(@RequestPart("image") MultipartFile image) throws IOException {

        return new ResponseEntity<>(
                SingleResponse.of(HttpStatus.CREATED, imageService.s3SaveImage(image)),
                HttpStatus.CREATED
        );
    }

}
