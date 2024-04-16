package com.blocker.blocker_server.contract.controller;

import com.blocker.blocker_server.ControllerTestSupport;
import com.blocker.blocker_server.contract.domain.CancelContractState;
import com.blocker.blocker_server.contract.dto.response.GetCancelContractWithSignStateResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(roles = "USER")
class CancelContractControllerTest extends ControllerTestSupport {

    @DisplayName("파기 진행 중 계약서 리스트를 조회한다.")
    @Test
    void getCancelingContractList() throws Exception {

        /** given */
        given(cancelContractService.getCancelContractList(any(CancelContractState.class))).willReturn(List.of());

        /** when */

        /** then */

        mockMvc.perform(get("/cancel-contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("state", "CANCELING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").isArray());

        verify(cancelContractService, times(1)).getCancelContractList(any(CancelContractState.class));

    }

    @DisplayName("파기완료된 계약서 리스트를 조회한다.")
    @Test
    void getCanceledContractList() throws Exception {

        /** given */

        given(cancelContractService.getCancelContractList(any(CancelContractState.class))).willReturn(List.of());

        /** when */

        /** then */

        mockMvc.perform(get("/cancel-contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("state", "CANCELED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").isArray());

        verify(cancelContractService, times(1)).getCancelContractList(any(CancelContractState.class));

    }

    @DisplayName("파기 진행 중 계약서를 조회한다.")
    @Test
    void getCancelingContract() throws Exception {

        /** given */

        GetCancelContractWithSignStateResponseDto response = GetCancelContractWithSignStateResponseDto.builder()
                .contractId(1l)
                .title("test")
                .content("test")
                .cancelContractId(1l)
                .contractorAndSignStates(List.of())
                .createdAt(LocalDateTime.of(2024, 1, 1, 12, 0))
                .modifiedAt(LocalDateTime.of(2024, 1, 1, 12, 0))
                .build();

        given(cancelContractService.getCancelingContract(anyLong())).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/cancel-contracts/canceling/{cancelContractId}", 1l)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data.contractId").value(1l))
                .andExpect(jsonPath("$.data.title").value("test"))
                .andExpect(jsonPath("$.data.content").value("test"))
                .andExpect(jsonPath("$.data.cancelContractId").value(1l))
                .andExpect(jsonPath("$.data.contractorAndSignStates").isArray())
                .andExpect(jsonPath("$.data.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.modifiedAt").isNotEmpty());

        verify(cancelContractService, times(1)).getCancelingContract(anyLong());

    }

    @DisplayName("파기완료된 계약서를 조회한다.")
    @Test
    void getCanceledContract() throws Exception {

        /** given */

        GetCancelContractWithSignStateResponseDto response = GetCancelContractWithSignStateResponseDto.builder()
                .contractId(1l)
                .title("test")
                .content("test")
                .cancelContractId(1l)
                .contractorAndSignStates(List.of())
                .createdAt(LocalDateTime.of(2024, 1, 1, 12, 0))
                .modifiedAt(LocalDateTime.of(2024, 1, 1, 12, 0))
                .build();

        given(cancelContractService.getCanceledContract(anyLong())).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/cancel-contracts/canceled/{cancelContractId}", 1l)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data.contractId").value(1l))
                .andExpect(jsonPath("$.data.title").value("test"))
                .andExpect(jsonPath("$.data.content").value("test"))
                .andExpect(jsonPath("$.data.cancelContractId").value(1l))
                .andExpect(jsonPath("$.data.contractorAndSignStates").isArray())
                .andExpect(jsonPath("$.data.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.modifiedAt").isNotEmpty());

        verify(cancelContractService, times(1)).getCanceledContract(anyLong());

    }

}