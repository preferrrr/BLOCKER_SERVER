package com.blocker.blocker_server.signature.controller;

import com.blocker.blocker_server.ControllerTestSupport;
import com.blocker.blocker_server.signature.dto.response.GetSignatureResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(roles = "USER")
class SignatureControllerTest extends ControllerTestSupport {

    @DisplayName("전자서명을 등록한다.")
    @Test
    void setSignature() throws Exception {

        /** given */

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer accessToken");

        given(signatureService.setSignature(any(), any(MultipartFile.class))).willReturn(headers);

        MockMultipartFile signature = new MockMultipartFile("signature", "file.jpeg",
                MediaType.IMAGE_JPEG_VALUE, "test".getBytes());


        /** when */

        /** then */

        mockMvc.perform(multipart("/signatures")
                        .file(signature)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));

        verify(signatureService, times(1)).setSignature(any(), any(MultipartFile.class));
    }

    @DisplayName("전자서명을 등록할 때, 파일을 보내지 않으면 204를 반환한다.")
    @Test
    void setSignatureNoContent() throws Exception {

        /** given */

        /** when */

        /** then */

        mockMvc.perform(multipart("/signatures")
                        .file("signature", (byte[]) null)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @DisplayName("전자서명을 수정한다.")
    @Test
    void modifySignature() throws Exception {

        /** given */

        willDoNothing().given(signatureService).modifySignature(any(), any(MultipartFile.class));

        MockMultipartFile signature = new MockMultipartFile("signature", "file.jpeg",
                MediaType.IMAGE_JPEG_VALUE, "test".getBytes());


        /** when */

        /** then */

        final MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .multipart("/signatures");
        builder.with(request -> {
            request.setMethod("PATCH");
            return request;
        });

        mockMvc.perform(builder.file(signature)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(signatureService, times(1)).modifySignature(any(), any(MultipartFile.class));
    }

    @DisplayName("전자서명을 수정하는데 이미지가 없으면 204을 반환한다.")
    @Test
    void modifySignatureNoContent() throws Exception {

        /** given */

        willDoNothing().given(signatureService).modifySignature(any(), any(MultipartFile.class));


        /** when */

        /** then */

        final MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .multipart("/signatures");
        builder.with(request -> {
            request.setMethod("PATCH");
            return request;
        });

        mockMvc.perform(builder.file("signature",(byte[]) null)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isNoContent());

    }

    @DisplayName("나의 전자서명을 조회한다.")
    @Test
    void getSignature() throws Exception {

        /** given */
        GetSignatureResponseDto response = GetSignatureResponseDto.builder()
                .address("test")
                .build();

        given(signatureService.getSignature(any())).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/signatures")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").exists());

    }
}