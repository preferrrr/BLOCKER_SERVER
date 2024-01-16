package com.blocker.blocker_server.Image.service;

import com.blocker.blocker_server.Image.dto.response.SaveImageResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;

    @Mock
    private S3Service s3Service;

    @DisplayName("s3에 이미지를 저장한다.")
    @Test
    void s3SaveImage() throws Exception{

        /** given */

        given(s3Service.saveImage(any(MultipartFile.class))).willReturn("test");

        /** when */

        SaveImageResponseDto result = imageService.s3SaveImage(mock(MultipartFile.class));

        /** then */

        verify(s3Service, times(1)).saveImage(any(MultipartFile.class));
        assertThat(result.getAddress()).isEqualTo("test");

    }

}