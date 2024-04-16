package com.blocker.blocker_server.contract.controller;

import com.blocker.blocker_server.ControllerTestSupport;
import com.blocker.blocker_server.contract.domain.ContractState;
import com.blocker.blocker_server.contract.dto.request.ModifyContractRequestDto;
import com.blocker.blocker_server.contract.dto.request.SaveContractRequestDto;
import com.blocker.blocker_server.contract.dto.response.*;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        willDoNothing().given(contractService).saveContract(any(SaveContractRequestDto.class));

        /** when */

        /** then */

        mockMvc.perform(post("/contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"));

        verify(contractService, times(1)).saveContract(any(SaveContractRequestDto.class));
    }


    @DisplayName("계약서 저장 request field가 null이면 InvalidRequestParameterException (400)을 반환한다.")
    @Test
    void saveContractInvalidField() throws Exception {

        /** given */

        SaveContractRequestDto request = SaveContractRequestDto.builder()
                .title(null)
                .content("test")
                .build();
        willDoNothing().given(contractService).saveContract(any(SaveContractRequestDto.class));

        /** when */

        /** then */

        mockMvc.perform(post("/contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("제목은 null 또는 공백일 수 없습니다."));

        verify(contractService, times(0)).saveContract(any(SaveContractRequestDto.class));

    }

    @DisplayName("계약서를 수정한다.")
    @Test
    void modifyContract() throws Exception {

        /** given */

        ModifyContractRequestDto request = ModifyContractRequestDto.builder()
                .title("test title")
                .content("test content")
                .build();
        willDoNothing().given(contractService).modifyContract(anyLong(), any(ModifyContractRequestDto.class));

        /** when */

        /** then */

        mockMvc.perform(patch("/contracts/{contractId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"));

        verify(contractService, times(1)).modifyContract(anyLong(), any(ModifyContractRequestDto.class));
    }


    @DisplayName("계약서 수정 request field가 null이면 InvalidRequestParameterException (400)을 반환한다.")
    @Test
    void modifyContractInvalidField() throws Exception {

        /** given */

        ModifyContractRequestDto request = ModifyContractRequestDto.builder()
                .title(null)
                .content("test")
                .build();
        willDoNothing().given(contractService).modifyContract(anyLong(), any(ModifyContractRequestDto.class));

        /** when */

        /** then */

        mockMvc.perform(patch("/contracts/{contractId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));

        verify(contractService, times(0)).modifyContract(anyLong(), any(ModifyContractRequestDto.class));
    }

    @DisplayName("미체결 계약서 리스트를 조회한다.")
    @Test
    void getNotProceedContracts() throws Exception {

        /** given */

        given(contractService.getContracts(any(ContractState.class))).willReturn(List.of());

        /** when */

        /** then */

        mockMvc.perform(get("/contracts")
                        .param("state", "NOT_PROCEED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").isArray());

        verify(contractService, times(1)).getContracts(any(ContractState.class));
    }

    @DisplayName("진행 중 계약서 리스트를 조회한다.")
    @Test
    void getProceedContracts() throws Exception {

        /** given */

        given(contractService.getContracts(any(ContractState.class))).willReturn(List.of());

        /** when */

        /** then */

        mockMvc.perform(get("/contracts")
                        .param("state", "PROCEED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").isArray());

        verify(contractService, times(1)).getContracts(any(ContractState.class));
    }

    @DisplayName("체결 계약서 리스트를 조회한다.")
    @Test
    void getConcludeContracts() throws Exception {

        /** given */

        given(contractService.getContracts(any(ContractState.class))).willReturn(List.of());

        /** when */

        /** then */

        mockMvc.perform(get("/contracts")
                        .param("state", "CONCLUDE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").isArray());

        verify(contractService, times(1)).getContracts(any(ContractState.class));
    }

    @DisplayName("계약서 리스트를 조회할 때 잘못된 쿼리스트링을 넣으면 InvalidQueryStringException (400)을 반환한다..")
    @Test
    void getContractsInvalidQueryString() throws Exception {

        /** given */

        given(contractService.getContracts(any(ContractState.class))).willReturn(List.of());

        /** when */

        /** then */

        mockMvc.perform(get("/contracts")
                        .param("state", "abcd")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(contractService, times(0)).getContracts(any(ContractState.class));

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
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.title").isNotEmpty())
                .andExpect(jsonPath("$.data.content").isNotEmpty())
                .andExpect(jsonPath("$.data.contractId").isNotEmpty())
                .andExpect(jsonPath("$.data.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.modifiedAt").isNotEmpty());

        verify(contractService, times(1)).getNotProceedContract(anyLong());
    }


    @DisplayName("미체결 계약서를 삭제한다.")
    @Test
    void deleteNotProceedContract() throws Exception {

        /** given */

        willDoNothing().given(contractService).deleteContract(anyLong());

        /** when */

        /** then */

        mockMvc.perform(delete("/contracts/{contractId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"));

        verify(contractService, times(1)).deleteContract(anyLong());

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

        given(contractService.getProceedContract(anyLong())).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/contracts/proceed/{contractId}", 1l)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.title").isNotEmpty())
                .andExpect(jsonPath("$.data.content").isNotEmpty())
                .andExpect(jsonPath("$.data.contractId").isNotEmpty())
                .andExpect(jsonPath("$.data.contractorAndSignStates").isArray())
                .andExpect(jsonPath("$.data.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.modifiedAt").isNotEmpty());

        verify(contractService, times(1)).getProceedContract(anyLong());

    }


    @DisplayName("계약서가 포함된 게시글까지 모두 delete한다.")
    @Test
    void deleteContractWithBoards() throws Exception {

        /** given */
        willDoNothing().given(contractService).deleteContractWithBoards(anyLong());

        /** when */

        /** then */

        mockMvc.perform(delete("/contracts/with-boards/{contractId}", 1l)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"));

        verify(contractService, times(1)).deleteContractWithBoards(anyLong());
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

        given(contractService.getConcludeContract(anyLong())).willReturn(response);
        /** when */

        /** then */

        mockMvc.perform(get("/contracts/conclude/{contractId}", 1l)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.title").isNotEmpty())
                .andExpect(jsonPath("$.data.content").isNotEmpty())
                .andExpect(jsonPath("$.data.contractId").isNotEmpty())
                .andExpect(jsonPath("$.data.contractorAndSignStates").isArray())
                .andExpect(jsonPath("$.data.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.modifiedAt").isNotEmpty());

        verify(contractService, times(1)).getConcludeContract(anyLong());

    }


}