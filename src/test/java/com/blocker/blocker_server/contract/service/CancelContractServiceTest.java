package com.blocker.blocker_server.contract.service;

import com.blocker.blocker_server.commons.utils.CurrentUserGetter;
import com.blocker.blocker_server.contract.domain.CancelContract;
import com.blocker.blocker_server.contract.domain.CancelContractState;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.dto.response.GetCancelContractWithSignStateResponseDto;
import com.blocker.blocker_server.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelContractServiceTest {

    @InjectMocks
    private CancelContractService cancelContractService;
    @Mock
    private CancelContractServiceSupport cancelContractServiceSupport;
    @Mock
    private CurrentUserGetter currentUserGetter;

    private User user;
    private Contract contract;
    private CancelContract cancelContract;

    @BeforeEach
    void setUp() {
        user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        contract = Contract.create(user, "testTitle", "testContent");
        cancelContract = CancelContract.create(user, contract, "testTitle", "testContent");
    }

    @DisplayName("파기 계약서 리스트를 조회한다.")
    @Test
    void getCancelContractList() {

        /** given */

        given(currentUserGetter.getCurrentUser()).willReturn(user);
        given(cancelContractServiceSupport.getCancelContractsByUserAndState(any(User.class), any(CancelContractState.class))).willReturn(mock(List.class));
        given(cancelContractServiceSupport.entityListToDtoList(anyList())).willReturn(mock(List.class));

        /** when */

        cancelContractService.getCancelContractList(CancelContractState.CANCELING);

        /** then */

        verify(cancelContractServiceSupport, times(1)).getCancelContractsByUserAndState(any(User.class), any(CancelContractState.class));
        verify(cancelContractServiceSupport, times(1)).entityListToDtoList(anyList());

    }

    @DisplayName("파기 진행 중 계약서를 조회한다.")
    @Test
    void getCancelingContract() {

        /** given */

        given(currentUserGetter.getCurrentUser()).willReturn(user);
        given(cancelContractServiceSupport.getCancelContractWithSignsById(anyLong())).willReturn(cancelContract);
        willDoNothing().given(cancelContractServiceSupport).checkIsCancelingCancelContract(any(CancelContract.class));
        willDoNothing().given(cancelContractServiceSupport).checkIsCancelContractParticipant(any(User.class), any(CancelContract.class));
        given(cancelContractServiceSupport.getCancelContractorAndSignState(anyList())).willReturn(mock(List.class));

        /** when */

        GetCancelContractWithSignStateResponseDto result = cancelContractService.getCancelingContract(1l);

        /** then */

        verify(cancelContractServiceSupport, times(1)).getCancelContractWithSignsById(anyLong());
        verify(cancelContractServiceSupport, times(1)).checkIsCancelingCancelContract(any(CancelContract.class));
        verify(cancelContractServiceSupport, times(1)).checkIsCancelContractParticipant(any(User.class), any(CancelContract.class));
        verify(cancelContractServiceSupport, times(1)).getCancelContractorAndSignState(anyList());



    }

    @DisplayName("파기 체결된 계약서를 조회한다.")
    @Test
    void getCanceledContract() {

        /** given */

        given(currentUserGetter.getCurrentUser()).willReturn(user);
        given(cancelContractServiceSupport.getCancelContractWithSignsById(anyLong())).willReturn(cancelContract);
        willDoNothing().given(cancelContractServiceSupport).checkIsCanceledCancelContract(any(CancelContract.class));
        willDoNothing().given(cancelContractServiceSupport).checkIsCancelContractParticipant(any(User.class), any(CancelContract.class));
        given(cancelContractServiceSupport.getCancelContractorAndSignState(anyList())).willReturn(mock(List.class));

        /** when */

        GetCancelContractWithSignStateResponseDto result = cancelContractService.getCanceledContract(1l);

        /** then */

        verify(cancelContractServiceSupport, times(1)).getCancelContractWithSignsById(anyLong());
        verify(cancelContractServiceSupport, times(1)).checkIsCanceledCancelContract(any(CancelContract.class));
        verify(cancelContractServiceSupport, times(1)).checkIsCancelContractParticipant(any(User.class), any(CancelContract.class));
        verify(cancelContractServiceSupport, times(1)).getCancelContractorAndSignState(anyList());

    }



}