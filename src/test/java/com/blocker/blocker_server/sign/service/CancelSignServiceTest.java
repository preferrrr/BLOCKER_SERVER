package com.blocker.blocker_server.sign.service;

import com.blocker.blocker_server.commons.utils.CurrentUserGetter;
import com.blocker.blocker_server.contract.domain.CancelContract;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.service.CancelContractServiceSupport;
import com.blocker.blocker_server.contract.service.ContractServiceSupport;
import com.blocker.blocker_server.sign.domain.CancelSign;
import com.blocker.blocker_server.sign.domain.SignState;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelSignServiceTest {

    @InjectMocks
    private CancelSignService cancelSignService;
    @Mock
    private CancelSignServiceSupport cancelSignServiceSupport;
    @Mock
    private ContractServiceSupport contractServiceSupport;
    @Mock
    private CancelContractServiceSupport cancelContractServiceSupport;
    @Mock
    private CurrentUserGetter currentUserGetter;

    private User user;
    private Contract contract;
    private CancelContract cancelContract;
    private CancelSign cancelSign;

    @BeforeEach
    void setUp() {
        user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        contract = Contract.create(user, "testTitle", "testContent");
        cancelContract = CancelContract.create(user, contract, "testTitle", "testContent");
        cancelSign = CancelSign.create(user, cancelContract);
    }

    @DisplayName("계약을 파기하면 CancelContact와 파기 계약 참여자들의 CancelSigns가 저장된다.")
    @Test
    void cancelContract() {

        /** given */

        given(currentUserGetter.getCurrentUser()).willReturn(user);
        given(contractServiceSupport.getContractWIthSignsById(anyLong())).willReturn(contract);
        willDoNothing().given(cancelSignServiceSupport).checkIsConcludeContract(any(Contract.class));
        willDoNothing().given(cancelSignServiceSupport).checkIsParticipantForCancel(any(User.class), any(Contract.class));
        willDoNothing().given(cancelSignServiceSupport).checkIsCancelingContract(any(Contract.class));
        given(cancelSignServiceSupport.createCancelSigns(any(CancelContract.class), anyList())).willReturn(mock(List.class));
        willDoNothing().given(cancelSignServiceSupport).saveCancelContract(any(CancelContract.class));
        willDoNothing().given(cancelSignServiceSupport).saveCancelSigns(anyList());

        /** when */

        cancelSignService.cancelContract(1l);

        /** then */

        verify(contractServiceSupport, times(1)).getContractWIthSignsById(anyLong());
        verify(cancelSignServiceSupport, times(1)).checkIsConcludeContract(any(Contract.class));
        verify(cancelSignServiceSupport, times(1)).checkIsParticipantForCancel(any(User.class), any(Contract.class));
        verify(cancelSignServiceSupport, times(1)).checkIsCancelingContract(any(Contract.class));
        verify(cancelSignServiceSupport, times(1)).createCancelSigns(any(CancelContract.class), anyList());
        verify(cancelSignServiceSupport, times(1)).saveCancelContract(any(CancelContract.class));
        verify(cancelSignServiceSupport, times(1)).saveCancelSigns(anyList());


    }

    @DisplayName("파기 계약에 서명하면 나의 CancelSign의 State가 Y로 바뀐다.")
    @Test
    void signCancelContract() {

        /** given */

        given(currentUserGetter.getCurrentUser()).willReturn(user);
        given(cancelContractServiceSupport.getCancelContractWithSignsById(anyLong())).willReturn(cancelContract);
        given(cancelSignServiceSupport.getMyCancelSign(anyString(), anyList())).willReturn(cancelSign);
        willDoNothing().given(cancelSignServiceSupport).checkMySignStateIsN(any(CancelSign.class));
        willDoNothing().given(cancelSignServiceSupport).checkIsAllAgree(any(CancelContract.class));

        /** when */

        cancelSignService.signCancelContract(1l);

        /** then */

        verify(cancelContractServiceSupport, times(1)).getCancelContractWithSignsById(anyLong());
        verify(cancelSignServiceSupport, times(1)).getMyCancelSign(anyString(), anyList());
        verify(cancelSignServiceSupport, times(1)).checkMySignStateIsN(any(CancelSign.class));
        verify(cancelSignServiceSupport, times(1)).checkIsAllAgree(any(CancelContract.class));

        assertThat(cancelSign.getSignState()).isEqualTo(SignState.Y);

    }

}