package com.blocker.blocker_server.board.service;

import com.blocker.blocker_server.board.dto.response.SaveImageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
@Transactional
public class ImageService {

    private final S3Service s3Service;

    public SaveImageResponseDto saveImage(MultipartFile image) throws IOException {

        String address = s3Service.saveImage(image);
        SaveImageResponseDto response = SaveImageResponseDto.builder()
                .address(address)
                .build();

        return response;

    }
}
