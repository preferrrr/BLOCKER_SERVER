package com.blocker.blocker_server.Image.controller;

import com.blocker.blocker_server.Image.dto.response.SaveImageResponseDto;
import com.blocker.blocker_server.Image.service.ImageService;
import com.blocker.blocker_server.commons.exception.InvalidImageException;
import com.blocker.blocker_server.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ImageController.class)
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ImageService imageService;

    @DisplayName("이미지를 s3에 저장하고, 저장된 주소를 반환한다.")
    @Test
    @WithMockUser(roles = "USER")
    void s3SaveImage() throws Exception {

        /** given */

        MockMultipartFile image = new MockMultipartFile("image", "file.jpeg",
                MediaType.IMAGE_JPEG_VALUE, "test".getBytes());

        SaveImageResponseDto response = SaveImageResponseDto.builder()
                .address("test")
                .build();

        given(imageService.s3SaveImage(any(MultipartFile.class))).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(multipart("/images")
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address").exists());

        verify(imageService, times(1)).s3SaveImage(any(MultipartFile.class));

    }

    @DisplayName("s3에 이미지를 저장할 때, 이미지가 없으면 204를 반환한다.")
    @Test
    @WithMockUser(roles = "USER")
    void s3SaveImageNoContent() throws Exception {

        /** given */

        /** when */

        /** then */

        mockMvc.perform(multipart("/images")
                        .file("image", (byte[]) null)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isNoContent());


    }

}