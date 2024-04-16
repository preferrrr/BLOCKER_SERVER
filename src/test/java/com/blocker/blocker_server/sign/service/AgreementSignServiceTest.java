package com.blocker.blocker_server.sign.service;

import com.blocker.blocker_server.chat.service.ChatService;
import com.blocker.blocker_server.commons.utils.CurrentUserGetter;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.domain.ContractState;
import com.blocker.blocker_server.contract.service.ContractServiceSupport;
import com.blocker.blocker_server.sign.domain.AgreementSign;
import com.blocker.blocker_server.sign.domain.SignState;
import com.blocker.blocker_server.sign.dto.request.ProceedSignRequestDto;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgreementSignServiceTest {

    @InjectMocks
    private AgreementSignService agreementSignService;

    @Mock
    private AgreementSignServiceSupport agreementSignServiceSupport;
    @Mock
    private ContractServiceSupport contractServiceSupport;
    @Mock
    private ChatService chatService;
    @Mock
    private CurrentUserGetter currentUserGetter;

    private User user;
    private Contract contract;
    private AgreementSign agreementSign;
    @BeforeEach
    void setUp() {
        user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        contract = Contract.create(user, "testTitle", "testContent");
        agreementSign = AgreementSign.create(user, contract);
    }

    @DisplayName("계약을 진행하고 ContractState가 PROCEED로 바뀐다.")
    @Test
    void proceedContract() {

        /** given */
        given(currentUserGetter.getCurrentUser()).willReturn(user);
        given(contractServiceSupport.getContractById(anyLong())).willReturn(contract);
        willDoNothing().given(contractServiceSupport).checkIsContractWriter(anyString(), any(Contract.class));
        willDoNothing().given(agreementSignServiceSupport).checkIsNotProceedContract(any(Contract.class));
        willDoNothing().given(agreementSignServiceSupport).checkIsEmptyContractor(anyList());
        given(agreementSignServiceSupport.createAgreementSigns(any(Contract.class), any(User.class), anyList())).willReturn(mock(List.class));
        willDoNothing().given(agreementSignServiceSupport).saveAgreementSigns(anyList());
        willDoNothing().given(chatService).createChatRoom(anyList());

        ProceedSignRequestDto request = ProceedSignRequestDto.builder()
                .contractId(1l)
                .contractors(List.of("testEmail2", "testEmail3"))
                .build();

        /** when */

        agreementSignService.proceedContract(request);

        /** then */

        verify(contractServiceSupport, times(1)).getContractById(anyLong());
        verify(contractServiceSupport, times(1)).checkIsContractWriter(anyString(), any(Contract.class));
        verify(agreementSignServiceSupport, times(1)).checkIsNotProceedContract(any(Contract.class));
        verify(agreementSignServiceSupport, times(1)).checkIsEmptyContractor(anyList());
        verify(agreementSignServiceSupport, times(1)).createAgreementSigns(any(Contract.class), any(User.class), anyList());
        verify(agreementSignServiceSupport, times(1)).saveAgreementSigns(anyList());
        verify(chatService, times(1)).createChatRoom(anyList());
        assertThat(contract.getContractState()).isEqualTo(ContractState.PROCEED);
    }

    @DisplayName("계약서의 내 AgreementSign에 서명하고 SignState가 Y로 바뀐다.")
    @Test
    void signContract() {

        /** given */

        given(currentUserGetter.getCurrentUser()).willReturn(user);
        given(contractServiceSupport.getContractWIthSignsById(anyLong())).willReturn(contract);
        given(agreementSignServiceSupport.getMyAgreementSign(anyString(), anyList())).willReturn(agreementSign);
        willDoNothing().given(agreementSignServiceSupport).checkMySignStateIsN(any(AgreementSign.class));
        willDoNothing().given(agreementSignServiceSupport).checkIsAllAgree(any(Contract.class));

        /** when */

        agreementSignService.signContract(1l);

        /** then */

        verify(contractServiceSupport, times(1)).getContractWIthSignsById(anyLong());
        verify(agreementSignServiceSupport, times(1)).getMyAgreementSign(anyString(), anyList());
        verify(agreementSignServiceSupport, times(1)).checkMySignStateIsN(any(AgreementSign.class));
        verify(agreementSignServiceSupport, times(1)).checkIsAllAgree(any(Contract.class));

        assertThat(agreementSign.getSignState()).isEqualTo(SignState.Y);

    }


    @DisplayName("계약 진행을 취소하고, 계약서 상태를 NOT_PROCEED로 바꾸며 참여자들의 AgreementSign들을 삭제한다.")
    @Test
    void breakContract() {

        /** given */

        given(currentUserGetter.getCurrentUser()).willReturn(user);
        given(contractServiceSupport.getContractWIthSignsById(anyLong())).willReturn(contract);
        willDoNothing().given(contractServiceSupport).checkIsParticipant(any(User.class), any(Contract.class));
        willDoNothing().given(contractServiceSupport).checkIsProceedContract(any(Contract.class));
        willDoNothing().given(agreementSignServiceSupport).deleteAgreementSigns(anyList());

        /** when */

        agreementSignService.breakContract(1l);

        /** then */

        verify(contractServiceSupport, times(1)).getContractWIthSignsById(anyLong());
        verify(contractServiceSupport, times(1)).checkIsParticipant(any(User.class), any(Contract.class));
        verify(contractServiceSupport, times(1)).checkIsProceedContract(any(Contract.class));
        verify(agreementSignServiceSupport, times(1)).deleteAgreementSigns(anyList());

        assertThat(contract.getContractState()).isEqualTo(ContractState.NOT_PROCEED);



    }

}