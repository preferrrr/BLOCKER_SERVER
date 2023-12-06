package com.blocker.blocker_server.board.controller;

import com.blocker.blocker_server.board.service.ImageService;
import com.blocker.blocker_server.board.dto.response.SaveImageResponseDto;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.commons.exception.InvalidImageException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<SaveImageResponseDto> saveImage(@AuthenticationPrincipal User user, @RequestPart("image") MultipartFile image) throws IOException {

        if(image.isEmpty())
            throw new InvalidImageException("email : " + user.getEmail());

        SaveImageResponseDto response = imageService.saveImage(image);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
