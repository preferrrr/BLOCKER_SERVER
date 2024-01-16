package com.blocker.blocker_server.sign.controller;

import com.blocker.blocker_server.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(roles = "USER")
class CancelSignControllerTest extends ControllerTestSupport {

    @DisplayName("계약을 파기한다. CancelContract와 CancelSign이 생성됨")
    @Test
    void cancelContract() throws Exception {

        /** given */

        willDoNothing().given(cancelSignService).cancelContract(any(), anyLong());

        /** when */

        /** then */

        mockMvc.perform(post("/cancel-signs/contract/{contractId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated());

        verify(cancelSignService, times(1)).cancelContract(any(), anyLong());

    }

    @DisplayName("파기 계약서에 서명한다. CancelSign의 state가 Y로 바뀐다.")
    @Test
    void signContract() throws Exception {

        /** given */

        willDoNothing().given(cancelSignService).signCancelContract(any(), anyLong());

        /** when */

        /** then */

        mockMvc.perform(patch("/cancel-signs/cancel-contract/{contractId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(cancelSignService, times(1)).signCancelContract(any(), anyLong());
    }

}