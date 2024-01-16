package com.blocker.blocker_server.sign.service;

import com.blocker.blocker_server.IntegrationTestSupport;
import com.blocker.blocker_server.contract.domain.CancelContract;
import com.blocker.blocker_server.contract.domain.CancelContractState;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.repository.CancelContractRepository;
import com.blocker.blocker_server.contract.repository.ContractRepository;
import com.blocker.blocker_server.sign.domain.AgreementSign;
import com.blocker.blocker_server.sign.domain.CancelSign;
import com.blocker.blocker_server.sign.domain.SignState;
import com.blocker.blocker_server.sign.exception.*;
import com.blocker.blocker_server.sign.repository.AgreementSignRepository;
import com.blocker.blocker_server.sign.repository.CancelSignRepository;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;


class CancelSignServiceSupportTest extends IntegrationTestSupport {


    @Autowired
    private CancelSignServiceSupport cancelSignServiceSupport;
    @Autowired
    private AgreementSignRepository agreementSignRepository;
    @Autowired
    private CancelContractRepository cancelContractRepository;
    @Autowired
    private CancelSignRepository cancelSignRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContractRepository contractRepository;

    @AfterEach
    void tearDown() {
        cancelSignRepository.deleteAllInBatch();
        cancelContractRepository.deleteAllInBatch();
        agreementSignRepository.deleteAllInBatch();
        contractRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("체결된 계약서가 아니라면 IsNotConcludeContractForCancelException을 던진다.")
    @Test
    void checkIsConcludeContract() {

        /** given */

        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);

        Contract NOT_PROCEED_contract = Contract.create(user, "testTitle", "testContent");
        Contract PROCEED_contract = Contract.create(user, "testTitle", "testContent");
        PROCEED_contract.updateStateToProceed();
        contractRepository.saveAll(List.of(NOT_PROCEED_contract, PROCEED_contract));

        /** when then */

        assertThatThrownBy(() -> cancelSignServiceSupport.checkIsConcludeContract(NOT_PROCEED_contract))
                .isInstanceOf(IsNotConcludeContractForCancelException.class);
        assertThatThrownBy(() -> cancelSignServiceSupport.checkIsConcludeContract(PROCEED_contract))
                .isInstanceOf(IsNotConcludeContractForCancelException.class);

    }

    @DisplayName("계약 파기 요청에서 계약서의 참여자가 아니면 IsNotContractParticipantForCancelException을 던진다.")
    @Test
    void checkIsParticipantForCancel() {

        /** given */
        User participant1 = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User participant2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User notParticipant = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(participant1, participant2, notParticipant));

        Contract contract = Contract.create(participant1, "testTitle", "testContent");
        contractRepository.save(contract);

        AgreementSign agreementSign1 = AgreementSign.create(participant1, contract);
        AgreementSign agreementSign2 = AgreementSign.create(participant2, contract);
        agreementSignRepository.saveAll(List.of(agreementSign1, agreementSign2));


        /** when then */

        assertThatThrownBy(() -> cancelSignServiceSupport.checkIsParticipantForCancel(notParticipant, contract))
                .isInstanceOf(IsNotContractParticipantForCancelException.class);

    }

    @DisplayName("CancelContract와 AgreementSign 리스트로 CancelSign 리스트를 만든다.")
    @Test
    void createCancelSigns() {

        /** given */
        User user1 = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2, user3));

        Contract contract = Contract.create(user1, "testTitle", "testContent");
        contractRepository.save(contract);

        AgreementSign agreementSign1 = AgreementSign.create(user1, contract);
        AgreementSign agreementSign2 = AgreementSign.create(user2, contract);
        AgreementSign agreementSign3 = AgreementSign.create(user3, contract);
        agreementSignRepository.saveAll(List.of(agreementSign1, agreementSign2, agreementSign3));

        CancelContract cancelContract = CancelContract.create(user1, contract, "testTitle", "testContent");
        cancelContractRepository.save(cancelContract);

        /** when */

        List<CancelSign> result = cancelSignServiceSupport.createCancelSigns(cancelContract, List.of(agreementSign1, agreementSign2, agreementSign3));

        /** then */

        assertThat(result).hasSize(3);

        List<String> participants = result.stream()
                .map(cancelSign -> cancelSign.getUser().getEmail())
                .collect(Collectors.toList());
        assertThat(participants.contains(user1.getEmail()));
        assertThat(participants.contains(user2.getEmail()));
        assertThat(participants.contains(user3.getEmail()));

        assertThat(result.get(0).getCancelContract().getCancelContractId()).isEqualTo(cancelContract.getCancelContractId());

    }

    @DisplayName("파기 계약서를 저장한다.")
    @Test
    void saveCancelContract() {

        /** given */
        User user1 = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user1);

        Contract contract = Contract.create(user1, "testTitle", "testContent");
        contractRepository.save(contract);

        CancelContract cancelContract = CancelContract.create(user1, contract, "testTitle", "testContent");

        /** when */

        cancelSignServiceSupport.saveCancelContract(cancelContract);

        /** then */

        List<CancelContract> cancelContracts = cancelContractRepository.findAll();
        assertThat(cancelContracts).hasSize(1);

    }

    @DisplayName("CancelSign 리스트를 저장한다.")
    @Test
    void saveCancelSigns() {

        /** given */
        User user1 = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2, user3));

        Contract contract = Contract.create(user1, "testTitle", "testContent");
        contractRepository.save(contract);

        CancelContract cancelContract = CancelContract.create(user1, contract, "testTitle", "testContent");
        cancelContractRepository.save(cancelContract);

        CancelSign cancelSign1 = CancelSign.create(user1, cancelContract);
        CancelSign cancelSign2 = CancelSign.create(user2, cancelContract);
        CancelSign cancelSign3 = CancelSign.create(user3, cancelContract);

        /** when */

        cancelSignServiceSupport.saveCancelSigns(List.of(cancelSign1, cancelSign2, cancelSign3));

        /** then */

        List<CancelSign> cancelSigns = cancelSignRepository.findAll();
        assertThat(cancelSigns).hasSize(3);
        assertThat(cancelSigns.get(0).getCancelContract().getCancelContractId()).isEqualTo(cancelContract.getCancelContractId());

        List<String> participants = cancelSigns.stream()
                .map(cancelSign -> cancelSign.getUser().getEmail())
                .collect(Collectors.toList());
        assertThat(participants.contains(user1.getEmail()));
        assertThat(participants.contains(user2.getEmail()));
        assertThat(participants.contains(user3.getEmail()));

    }

    @DisplayName("CancelSign 리스트 중 나의 CancelSign을 반환한다.")
    @Test
    void getMyCancelSign() {

        /** given */
        User user1 = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2, user3));

        Contract contract = Contract.create(user1, "testTitle", "testContent");
        contractRepository.save(contract);

        CancelContract cancelContract = CancelContract.create(user1, contract, "testTitle", "testContent");
        cancelContractRepository.save(cancelContract);

        CancelSign cancelSign1 = CancelSign.create(user1, cancelContract);
        CancelSign cancelSign2 = CancelSign.create(user2, cancelContract);
        CancelSign cancelSign3 = CancelSign.create(user3, cancelContract);
        cancelSignRepository.saveAll(List.of(cancelSign1, cancelSign2, cancelSign3));

        /** when */

        CancelSign myCancelSign = cancelSignServiceSupport.getMyCancelSign(user1.getEmail(), List.of(cancelSign1, cancelSign2, cancelSign3));

        /** then */

        assertThat(myCancelSign.getUser().getEmail()).isEqualTo(user1.getEmail());

    }

    @DisplayName("CancelSign 리스트 중 나의 CancelSign이 없으면, IsNotCancelContractParticipantException을 던진다.")
    @Test
    void getMyCancelSignException() {

        /** given */
        User me = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User contractor1 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User contractor2 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(me, contractor1, contractor2));

        Contract contract = Contract.create(me, "testTitle", "testContent");
        contractRepository.save(contract);

        CancelContract cancelContract = CancelContract.create(me, contract, "testTitle", "testContent");
        cancelContractRepository.save(cancelContract);

        CancelSign cancelSign1 = CancelSign.create(contractor1, cancelContract);
        CancelSign cancelSign2 = CancelSign.create(contractor2, cancelContract);
        cancelSignRepository.saveAll(List.of(cancelSign1, cancelSign2));

        /** when then */

        assertThatThrownBy(() -> cancelSignServiceSupport.getMyCancelSign(me.getEmail(), List.of(cancelSign1, cancelSign2)))
                .isInstanceOf(IsNotCancelContractParticipantException.class);

    }

    @DisplayName("CancelSign들의 SignState가 모두 Y이면, CancelContract의 CancelContractState를 CANCELED로 바꾼다.")
    @Test
    void checkIsAllAgree() {

        /** given */
        User user1 = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2, user3));

        Contract contract = Contract.create(user1, "testTitle", "testContent");
        contractRepository.save(contract);

        CancelContract cancelContract = CancelContract.create(user1, contract, "testTitle", "testContent");
        cancelContractRepository.save(cancelContract);

        CancelSign cancelSign1 = CancelSign.create(user1, cancelContract);
        cancelSign1.sign();
        CancelSign cancelSign2 = CancelSign.create(user2, cancelContract);
        cancelSign2.sign();
        CancelSign cancelSign3 = CancelSign.create(user3, cancelContract);
        cancelSign3.sign();
        cancelSignRepository.saveAll(List.of(cancelSign1, cancelSign2, cancelSign3));

        cancelContract = cancelContractRepository.findCancelContractWithSignsById(cancelContract.getCancelContractId()).get();

        /** when */

        cancelSignServiceSupport.checkIsAllAgree(cancelContract);

        /** then */

        assertThat(cancelContract.getCancelContractState()).isEqualTo(CancelContractState.CANCELED);
    }

    @DisplayName("나의 CancelSign의 state가 Y이면 IsAlreadyCancelSignException을 던진다.")
    @Test
    void checkMySignStateIsN() {

        /** given */
        User me = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User user = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        userRepository.saveAll(List.of(me, user));

        Contract contract = Contract.create(me, "testTitle", "testContent");
        contractRepository.save(contract);

        CancelContract cancelContract = CancelContract.create(me, contract, "testTitle", "testContent");
        cancelContractRepository.save(cancelContract);

        CancelSign myCancelSign = CancelSign.create(me, cancelContract);
        myCancelSign.sign();
        CancelSign userCancelSign = CancelSign.create(user, cancelContract);
        cancelSignRepository.saveAll(List.of(myCancelSign, userCancelSign));

        /** when then */

        assertThatThrownBy(() -> cancelSignServiceSupport.checkMySignStateIsN(myCancelSign))
                .isInstanceOf(IsAlreadyCancelSignException.class);
    }

    @DisplayName("이미 파기 진행 중 계약서라면 IsAlreadyCancelingException을 던진다.")
    @Test
    void checkIsCancelingContract() {

        /** given */
        User user1 = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2));

        Contract contract = Contract.create(user1, "testTitle", "testContent");
        contractRepository.save(contract);

        CancelContract cancelContract = CancelContract.create(user1, contract, "testTitle", "testContent");
        cancelContractRepository.save(cancelContract);

        CancelSign cancelSign1 = CancelSign.create(user1, cancelContract);
        cancelSign1.sign();
        CancelSign cancelSign2 = CancelSign.create(user2, cancelContract);
        cancelSignRepository.saveAll(List.of(cancelSign1, cancelSign2));

        /** when then */
        assertThatThrownBy(() -> cancelSignServiceSupport.checkIsCancelingContract(contract))
                .isInstanceOf(IsAlreadyCancelingException.class);
    }

}