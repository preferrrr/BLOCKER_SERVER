package com.blocker.blocker_server.contract.controller;

import com.blocker.blocker_server.ControllerTestSupport;
import com.blocker.blocker_server.contract.domain.ContractState;
import com.blocker.blocker_server.contract.dto.request.ModifyContractRequestDto;
import com.blocker.blocker_server.contract.dto.request.SaveContractRequestDto;
import com.blocker.blocker_server.contract.dto.response.*;
import com.blocker.blocker_server.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(roles = "USER")
class ContractControllerTest extends ControllerTestSupport {

    @DisplayName("계약서를 저장한다.")
    @Test
    void saveContract() throws Exception {

        /** given */

        SaveContractRequestDto request = SaveContractRequestDto.builder()
                .title("test")
                .content("test")
                .build();
        willDoNothing().given(contractService).saveContract(any(), any(SaveContractRequestDto.class));

        /** when */

        /** then */

        mockMvc.perform(post("/contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated());

        verify(contractService, times(1)).saveContract(any(), any(SaveContractRequestDto.class));
    }


    @DisplayName("계약서 저장 request field가 null이면 InvalidRequestParameterException (204)을 반환한다.")
    @Test
    void saveContractInvalidField() throws Exception {

        /** given */

        SaveContractRequestDto request = SaveContractRequestDto.builder()
                .title(null)
                .content("test")
                .build();
        willDoNothing().given(contractService).saveContract(any(), any(SaveContractRequestDto.class));

        /** when */

        /** then */

        mockMvc.perform(post("/contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isNoContent());

    }

    @DisplayName("계약서를 수정한다.")
    @Test
    void modifyContract() throws Exception {

        /** given */

        ModifyContractRequestDto request = ModifyContractRequestDto.builder()
                .title("test")
                .content("test")
                .build();
        willDoNothing().given(contractService).modifyContract(any(), anyLong(), any(ModifyContractRequestDto.class));

        /** when */

        /** then */

        mockMvc.perform(patch("/contracts/{contractId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(contractService, times(1)).modifyContract(any(), anyLong(), any(ModifyContractRequestDto.class));
    }


    @DisplayName("계약서 수정 request field가 null이면 InvalidRequestParameterException (204)을 반환한다.")
    @Test
    void modifyContractInvalidField() throws Exception {

        /** given */

        ModifyContractRequestDto request = ModifyContractRequestDto.builder()
                .title(null)
                .content("test")
                .build();
        willDoNothing().given(contractService).modifyContract(any(User.class), anyLong(), any(ModifyContractRequestDto.class));

        /** when */

        /** then */

        mockMvc.perform(patch("/contracts/{contractId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @DisplayName("미체결 계약서 리스트를 조회한다.")
    @Test
    void getNotProceedContracts() throws Exception {

        /** given */

        List<GetContractResponseDto> response = List.of();
        given(contractService.getContracts(any(), any(ContractState.class))).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/contracts")
                        .param("state", "NOT_PROCEED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(contractService, times(1)).getContracts(any(), any(ContractState.class));
    }

    @DisplayName("진행 중 계약서 리스트를 조회한다.")
    @Test
    void getProceedContracts() throws Exception {

        /** given */

        List<GetContractResponseDto> response = List.of();
        given(contractService.getContracts(any(), any(ContractState.class))).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/contracts")
                        .param("state", "PROCEED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(contractService, times(1)).getContracts(any(), any(ContractState.class));
    }

    @DisplayName("체결 계약서 리스트를 조회한다.")
    @Test
    void getConcludeContracts() throws Exception {

        /** given */

        List<GetContractResponseDto> response = List.of();
        given(contractService.getContracts(any(User.class), any(ContractState.class))).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/contracts")
                        .param("state", "CONCLUDE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(contractService, times(1)).getContracts(any(), any(ContractState.class));
    }

    @DisplayName("계약서 리스트를 조회할 때 잘못된 쿼리스트링을 넣으면 InvalidQueryStringException (400)을 반환한다..")
    @Test
    void getContractsInvalidQueryString() throws Exception {

        /** given */

        List<GetContractResponseDto> response = List.of();
        given(contractService.getContracts(any(User.class), any(ContractState.class))).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/contracts")
                        .param("state", "invalidQueryString")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @DisplayName("미체결 계약서를 조회한다.")
    @Test
    void getNotProceedContract() throws Exception {

        /** given */

        GetContractResponseDto response = GetContractResponseDto.builder()
                .title("test")
                .content("test")
                .contractId(1l)
                .createdAt(LocalDateTime.of(2024, 1, 1, 12, 0))
                .modifiedAt(LocalDateTime.of(2024, 1, 1, 12, 0))
                .build();
        given(contractService.getNotProceedContract(anyLong())).willReturn(response);

        /** when */

        /** then */
        mockMvc.perform(get("/contracts/not-proceed/{contractId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.contractId").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.modifiedAt").exists());

        verify(contractService, times(1)).getNotProceedContract(anyLong());
    }


    @DisplayName("미체결 계약서를 삭제한다.")
    @Test
    void deleteNotProceedContract() throws Exception {

        /** given */

        willDoNothing().given(contractService).deleteContract(any(), anyLong());

        /** when */

        /** then */

        mockMvc.perform(delete("/contracts/{contractId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(contractService, times(1)).deleteContract(any(), anyLong());

    }


    @DisplayName("진행 중 계약서를 조회한다.")
    @Test
    void getProceedContract() throws Exception {

        /** given */

        GetProceedContractResponseDto response = GetProceedContractResponseDto.builder()
                .title("test")
                .content("test")
                .contractId(1l)
                .contractorAndSignStates(List.of())
                .createdAt(LocalDateTime.of(2024, 1, 1, 12, 0))
                .modifiedAt(LocalDateTime.of(2024, 1, 1, 12, 0))
                .build();

        given(contractService.getProceedContract(any(), anyLong())).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/contracts/proceed/{contractId}", 1l)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.contractId").exists())
                .andExpect(jsonPath("$.contractorAndSignStates").isArray())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.modifiedAt").exists());

        verify(contractService, times(1)).getProceedContract(any(), anyLong());

    }


    @DisplayName("계약서가 포함된 게시글까지 모두 delete한다.")
    @Test
    void deleteContractWithBoards() throws Exception {

        /** given */
        willDoNothing().given(contractService).deleteContractWithBoards(any(), anyLong());

        /** when */

        /** then */

        mockMvc.perform(delete("/contracts/with-boards/{contractId}", 1l)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(contractService, times(1)).deleteContractWithBoards(any(), anyLong());
    }

    @DisplayName("체결 계약서를 조회한다.")
    @Test
    void getConcludeContract() throws Exception {

        /** given */
        GetConcludeContractResponseDto response = GetConcludeContractResponseDto.builder()
                .title("test")
                .content("test")
                .contractId(1l)
                .contractorAndSignStates(List.of())
                .createdAt(LocalDateTime.of(2024, 1, 1, 12, 0))
                .modifiedAt(LocalDateTime.of(2024, 1, 1, 12, 0))
                .build();

        given(contractService.getConcludeContract(any(), anyLong())).willReturn(response);
        /** when */

        /** then */

        mockMvc.perform(get("/contracts/conclude/{contractId}", 1l)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.contractId").exists())
                .andExpect(jsonPath("$.contractorAndSignStates").isArray())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.modifiedAt").exists());

        verify(contractService, times(1)).getConcludeContract(any(), anyLong());

    }


}