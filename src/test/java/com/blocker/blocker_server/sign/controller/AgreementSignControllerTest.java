package com.blocker.blocker_server.sign.controller;

import com.blocker.blocker_server.ControllerTestSupport;
import com.blocker.blocker_server.sign.dto.request.ProceedSignRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(roles = "USER")
class AgreementSignControllerTest extends ControllerTestSupport {

    @DisplayName("계약을 진행한다.")
    @Test
    void proceedContract() throws Exception {

        /** given */

        willDoNothing().given(agreementSignService).proceedContract(any(), any(ProceedSignRequestDto.class));

        ProceedSignRequestDto request = ProceedSignRequestDto.builder()
                .contractId(1l)
                .contractors(List.of("testEmail", "testEmail2"))
                .build();

        /** when */

        /** then */

        mockMvc.perform(post("/agreement-signs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated());
        verify(agreementSignService, times(1)).proceedContract(any(), any(ProceedSignRequestDto.class));
    }

    @DisplayName("계약 진행 request dto field가 비어있으면 InvalidRequestParameterException (204)을 반환한다.")
    @Test
    void proceedContractInvalidField() throws Exception {

        /** given */

        willDoNothing().given(agreementSignService).proceedContract(any(), any(ProceedSignRequestDto.class));

        ProceedSignRequestDto request = ProceedSignRequestDto.builder()
                .contractId(null)
                .contractors(List.of("testEmail", "testEmail2"))
                .build();

        /** when */

        /** then */

        mockMvc.perform(post("/agreement-signs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @DisplayName("계약서에 서명한다. agreementSign의 state가 Y로 바뀜.")
    @Test
    void signContract() throws Exception{

        /** given */

        willDoNothing().given(agreementSignService).signContract(any(),anyLong());

        /** when */

        /** then */

        mockMvc.perform(patch("/agreement-signs/contract/{contractId}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk());

        verify(agreementSignService, times(1)).signContract(any(), anyLong());

    }

    @DisplayName("진행 중인 계약을 파기한다. 계약서의 state가 NOT_PROCEED로 바뀌며, AgreementSign이 지워진다.")
    @Test
    void breakContract() throws Exception {

        /** given */

        willDoNothing().given(agreementSignService).breakContract(any(),anyLong());

        /** when */

        /** then */

        mockMvc.perform(delete("/agreement-signs/contract/{contractId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk());

        verify(agreementSignService, times(1)).breakContract(any(), anyLong());
    }

}