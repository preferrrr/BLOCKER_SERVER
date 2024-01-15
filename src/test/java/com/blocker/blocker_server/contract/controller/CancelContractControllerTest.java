package com.blocker.blocker_server.contract.controller;

import com.blocker.blocker_server.contract.domain.CancelContractState;
import com.blocker.blocker_server.contract.dto.response.GetCancelContractResponseDto;
import com.blocker.blocker_server.contract.dto.response.GetCancelContractWithSignStateResponseDto;
import com.blocker.blocker_server.contract.service.CancelContractService;
import com.blocker.blocker_server.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CancelContractController.class)
@WithMockUser(roles = "USER")
class CancelContractControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CancelContractService cancelContractService;

    @DisplayName("파기 진행 중 계약서 리스트를 조회한다.")
    @Test
    void getCancelingContractList() throws Exception {

        /** given */
        List<GetCancelContractResponseDto> response = List.of();
        given(cancelContractService.getCancelContractList(any(), any(CancelContractState.class))).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/cancel-contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("state", "CANCELING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(cancelContractService, times(1)).getCancelContractList(any(), any(CancelContractState.class));

    }

    @DisplayName("파기완료된 계약서 리스트를 조회한다.")
    @Test
    void getCanceledContractList() throws Exception {

        /** given */
        List<GetCancelContractResponseDto> response = List.of();
        given(cancelContractService.getCancelContractList(any(), any(CancelContractState.class))).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/cancel-contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("state", "CANCELED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(cancelContractService, times(1)).getCancelContractList(any(), any(CancelContractState.class));

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

        given(cancelContractService.getCancelingContract(any(), anyLong())).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/cancel-contracts/canceling/{cancelContractId}", 1l)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contractId").exists())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.cancelContractId").exists())
                .andExpect(jsonPath("$.contractorAndSignStates").isArray())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.modifiedAt").exists());

        verify(cancelContractService, times(1)).getCancelingContract(any(), anyLong());

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

        given(cancelContractService.getCanceledContract(any(), anyLong())).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/cancel-contracts/canceled/{cancelContractId}", 1l)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contractId").exists())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.cancelContractId").exists())
                .andExpect(jsonPath("$.contractorAndSignStates").isArray())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.modifiedAt").exists());

        verify(cancelContractService, times(1)).getCanceledContract(any(), anyLong());

    }

}