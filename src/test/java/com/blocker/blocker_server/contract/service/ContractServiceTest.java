package com.blocker.blocker_server.contract.service;

import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.domain.ContractState;
import com.blocker.blocker_server.contract.dto.request.ModifyContractRequestDto;
import com.blocker.blocker_server.contract.dto.request.SaveContractRequestDto;
import com.blocker.blocker_server.contract.dto.response.GetConcludeContractResponseDto;
import com.blocker.blocker_server.contract.dto.response.GetContractResponseDto;
import com.blocker.blocker_server.contract.dto.response.GetProceedContractResponseDto;
import com.blocker.blocker_server.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @InjectMocks
    private ContractService contractService;
    @Mock
    private ContractServiceSupport contractServiceSupport;

    private Contract contract;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        contract = Contract.create(user, "testTitle", "testContent");
    }

    @DisplayName("계약서를 저장한다.")
    @Test
    void saveContract() {

        /** given */

        willDoNothing().given(contractServiceSupport).saveContract(any(Contract.class));
        SaveContractRequestDto requestDto = SaveContractRequestDto.builder()
                .title("testTitle")
                .content("testContent")
                .build();

        /** when */

        contractService.saveContract(user, requestDto);

        /** then */

        verify(contractServiceSupport, times(1)).saveContract(any(Contract.class));

    }

    @DisplayName("계약서를 수정한다.")
    @Test
    void modifyBoard() {

        /** given */
        given(contractServiceSupport.getContractById(anyLong())).willReturn(contract);
        willDoNothing().given(contractServiceSupport).checkIsConcludeContractForModify(anyString(), any(Contract.class));
        willDoNothing().given(contractServiceSupport).checkIsProceedContractForModify(any(Contract.class));

        ModifyContractRequestDto requestDto = ModifyContractRequestDto.builder()
                .title("testTitle2")
                .content("testContent2")
                .build();

        /** when */

        contractService.modifyContract(user, 1l, requestDto);

        /** then */
        verify(contractServiceSupport, times(1)).getContractById(anyLong());
        verify(contractServiceSupport, times(1)).checkIsConcludeContractForModify(anyString(), any(Contract.class));
        verify(contractServiceSupport, times(1)).checkIsProceedContractForModify(any(Contract.class));

        assertThat(contract.getTitle()).isEqualTo("testTitle2");
        assertThat(contract.getContent()).isEqualTo("testContent2");
    }

    @DisplayName("계약서 리스트를 반환한다.")
    @Test
    void getContracts() {

        /** given */

        given(contractServiceSupport.getContractsByUserAndState(anyString(), any(ContractState.class))).willReturn(mock(List.class));
        given(contractServiceSupport.entityListToDtoList(anyList())).willReturn(mock(List.class));

        /** when */

        contractService.getContracts(user, ContractState.NOT_PROCEED);

        /** then */

        verify(contractServiceSupport, times(1)).getContractsByUserAndState(anyString(), any(ContractState.class));
        verify(contractServiceSupport, times(1)).entityListToDtoList(anyList());

    }

    @DisplayName("미체결 계약서를 조회한다.")
    @Test
    void getNotProceedContract() {

        /** given */
        given(contractServiceSupport.getContractById(anyLong())).willReturn(contract);
        willDoNothing().given(contractServiceSupport).checkIsNotProceedContract(any(Contract.class));

        /** when */

        GetContractResponseDto result = contractService.getNotProceedContract(1l);

        /** then */
        verify(contractServiceSupport, times(1)).getContractById(anyLong());
        verify(contractServiceSupport, times(1)).checkIsNotProceedContract(any(Contract.class));

        assertThat(result.getTitle()).isEqualTo(contract.getTitle());
        assertThat(result.getContent()).isEqualTo(contract.getContent());

    }

    @DisplayName("계약서를 삭제한다.")
    @Test
    void deleteContract() {

        /** given */

        given(contractServiceSupport.getContractById(anyLong())).willReturn(mock(Contract.class));
        willDoNothing().given(contractServiceSupport).checkIsContractWriter(anyString(), any(Contract.class));
        willDoNothing().given(contractServiceSupport).checkIsNotProceedContract(any(Contract.class));
        willDoNothing().given(contractServiceSupport).checkExistsBoardBelongingToContract(any(Contract.class));
        willDoNothing().given(contractServiceSupport).deleteContractById(anyLong());

        /** when */

        contractService.deleteContract(user, 1l);

        /** then */

        verify(contractServiceSupport, times(1)).getContractById(anyLong());
        verify(contractServiceSupport, times(1)).checkIsContractWriter(anyString(), any(Contract.class));
        verify(contractServiceSupport, times(1)).checkIsNotProceedContract(any(Contract.class));
        verify(contractServiceSupport, times(1)).checkExistsBoardBelongingToContract(any(Contract.class));
        verify(contractServiceSupport, times(1)).deleteContractById(anyLong());


    }

    @DisplayName("계약서와 해당 계약서가 포함된 게시글을 모두 지운다.")
    @Test
    void deleteContractWithBoards() {

        /** given */

        given(contractServiceSupport.getContractById(anyLong())).willReturn(contract);
        willDoNothing().given(contractServiceSupport).checkIsContractWriter(anyString(), any(Contract.class));
        willDoNothing().given(contractServiceSupport).checkIsNotProceedContract(any(Contract.class));
        willDoNothing().given(contractServiceSupport).deleteContractById(anyLong());

        /** when */

        contractService.deleteContractWithBoards(user, 1l);

        /** then */
        verify(contractServiceSupport, times(1)).getContractById(anyLong());
        verify(contractServiceSupport, times(1)).checkIsContractWriter(anyString(), any(Contract.class));
        verify(contractServiceSupport, times(1)).checkIsNotProceedContract(any(Contract.class));
        verify(contractServiceSupport, times(1)).deleteContractById(anyLong());


    }

    @DisplayName("진행 중 계약서를 조회한다.")
    @Test
    void getProceedContract() {

        /** given */

        given(contractServiceSupport.getContractWIthSignsById(anyLong())).willReturn(contract);
        willDoNothing().given(contractServiceSupport).checkIsProceedContract(any(Contract.class));
        willDoNothing().given(contractServiceSupport).checkIsParticipant(any(User.class), any(Contract.class));
        given(contractServiceSupport.getContractorAndSignState(anyList())).willReturn(mock(List.class));

        /** when */

        GetProceedContractResponseDto result = contractService.getProceedContract(user, 1l);

        /** then */

        verify(contractServiceSupport, times(1)).getContractWIthSignsById(anyLong());
        verify(contractServiceSupport, times(1)).checkIsProceedContract(any(Contract.class));
        verify(contractServiceSupport, times(1)).checkIsParticipant(any(User.class), any(Contract.class));
        verify(contractServiceSupport, times(1)).getContractorAndSignState(anyList());

    }

    @DisplayName("체결 계약서를 조회한다.")
    @Test
    void getConcludeContract() {

        /** given */

        given(contractServiceSupport.getContractWIthSignsById(anyLong())).willReturn(contract);
        willDoNothing().given(contractServiceSupport).checkIsConcludeContract(any(Contract.class));
        willDoNothing().given(contractServiceSupport).checkIsParticipant(any(User.class), any(Contract.class));
        given(contractServiceSupport.getContractorAndSignState(anyList())).willReturn(mock(List.class));

        /** when */

        GetConcludeContractResponseDto result = contractService.getConcludeContract(user, 1l);

        /** then */

        verify(contractServiceSupport, times(1)).getContractWIthSignsById(anyLong());
        verify(contractServiceSupport, times(1)).checkIsConcludeContract(any(Contract.class));
        verify(contractServiceSupport, times(1)).checkIsParticipant(any(User.class), any(Contract.class));
        verify(contractServiceSupport, times(1)).getContractorAndSignState(anyList());

    }

}