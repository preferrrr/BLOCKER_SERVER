package com.blocker.blocker_server.Image.service;

import com.blocker.blocker_server.Image.dto.response.SaveImageResponseDto;
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
    private final ImageServiceSupport imageServiceSupport;

    public SaveImageResponseDto s3SaveImage(MultipartFile image) throws IOException {

        String address = s3Service.saveImage(image);

        return SaveImageResponseDto.of(address);
    }
}
