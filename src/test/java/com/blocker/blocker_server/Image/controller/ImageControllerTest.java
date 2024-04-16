package com.blocker.blocker_server.Image.controller;

import com.blocker.blocker_server.ControllerTestSupport;
import com.blocker.blocker_server.Image.dto.response.SaveImageResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(roles = "USER")
class ImageControllerTest extends ControllerTestSupport {

    @DisplayName("이미지를 s3에 저장하고, 저장된 주소를 반환한다.")
    @Test
    void s3SaveImage() throws Exception {

        /** given */

        MockMultipartFile image = new MockMultipartFile("image", "file.jpeg",
                MediaType.IMAGE_JPEG_VALUE, "test".getBytes());

        SaveImageResponseDto response = SaveImageResponseDto.builder()
                .address("test address")
                .build();

        given(imageService.s3SaveImage(any(MultipartFile.class))).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(multipart("/images")
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.data.address").value("test address"));

        verify(imageService, times(1)).s3SaveImage(any(MultipartFile.class));

    }

    @DisplayName("s3에 이미지를 저장할 때, 이미지가 없으면 400을 반환한다.")
    @Test
    void s3SaveImageNoContent() throws Exception {

        /** given */

        /** when */

        /** then */

        mockMvc.perform(multipart("/images")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(signatureService, times(0)).setSignature(any(MultipartFile.class));


    }

}