package com.blocker.blocker_server.sign.service;

import com.blocker.blocker_server.IntegrationTestSupport;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.domain.ContractState;
import com.blocker.blocker_server.contract.exception.IsNotContractParticipantException;
import com.blocker.blocker_server.contract.exception.IsNotNotProceedContractException;
import com.blocker.blocker_server.contract.repository.ContractRepository;
import com.blocker.blocker_server.sign.domain.AgreementSign;
import com.blocker.blocker_server.sign.domain.SignState;
import com.blocker.blocker_server.sign.exception.EmptyParticipantException;
import com.blocker.blocker_server.sign.exception.IsAlreadySignedException;
import com.blocker.blocker_server.sign.repository.AgreementSignRepository;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.exception.UserNotFoundException;
import com.blocker.blocker_server.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

class AgreementSignServiceSupportTest extends IntegrationTestSupport {

    @Autowired
    private AgreementSignServiceSupport agreementSignServiceSupport;

    @Autowired
    private AgreementSignRepository agreementSignRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContractRepository contractRepository;


    @AfterEach
    void tearDown() {
        agreementSignRepository.deleteAllInBatch();
        contractRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }


    @DisplayName("AgreementSign들의 SignState를 모두 N으로 바꾼다.")
    @Test
    void modifySignsToN() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user, user2, user3));

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        AgreementSign agreementSign1 = AgreementSign.create(user, contract);
        agreementSign1.sign();
        AgreementSign agreementSign2 = AgreementSign.create(user2, contract);
        agreementSign2.sign();
        AgreementSign agreementSign3 = AgreementSign.create(user3, contract);
        agreementSign3.sign();
        agreementSignRepository.saveAll(List.of(agreementSign1, agreementSign2, agreementSign3));

        /** when */

        agreementSignServiceSupport.modifySignsToN(contract);

        /** then */

        List<AgreementSign> agreementSigns = agreementSignRepository.findByContract(contract);

        assertThat(agreementSigns).hasSize(3);
        assertThat(agreementSigns.get(0).getSignState()).isEqualTo(SignState.N);
        assertThat(agreementSigns.get(1).getSignState()).isEqualTo(SignState.N);
        assertThat(agreementSigns.get(2).getSignState()).isEqualTo(SignState.N);

    }

    @DisplayName("계약서의 상태가 NOT_PROCEED가 아니면 IsNotNotProceedContractException을 던진다.")
    @Test
    void checkIsNotProceedContract() {

        /** given */

        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);

        Contract contract1 = Contract.create(user, "testTitle", "testContent");
        Contract contract2 = Contract.create(user, "testTitle2", "testContent2");
        contract1.updateStateToProceed();
        contract2.updateStateToConclude();
        contractRepository.saveAll(List.of(contract1, contract2));


        /** when then */

        assertThatThrownBy(() -> agreementSignServiceSupport.checkIsNotProceedContract(contract1))
                .isInstanceOf(IsNotNotProceedContractException.class);

        assertThatThrownBy(() -> agreementSignServiceSupport.checkIsNotProceedContract(contract2))
                .isInstanceOf(IsNotNotProceedContractException.class);
    }

    @DisplayName("계약에 참여하는 사람들의 리스트가 비어있으면 EmptyParticipantException을 던진다.")
    @Test
    void checkIsEmptyContractor() {

        /** given */

        List<String> contractors = new ArrayList<>();

        /** when then */

        assertThatThrownBy(() -> agreementSignServiceSupport.checkIsEmptyContractor(contractors))
                .isInstanceOf(EmptyParticipantException.class);
    }

    @DisplayName("계약서, 나, 참여자들을 입력받아 AgreementSign 리스트를 반환한다.")
    @Test
    void createAgreementSigns() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user, user2, user3));

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        /** when */

        List<AgreementSign> agreementSigns = agreementSignServiceSupport.createAgreementSigns(contract, user, List.of("testEmail2", "testEmail3"));

        /** then */

        assertThat(agreementSigns).hasSize(3);
        assertThat(agreementSigns.get(0).getContract().getContractId()).isEqualTo(contract.getContractId());

        List<String> contractors = agreementSigns.stream()
                .map(contractor -> contractor.getUser().getEmail())
                .collect(Collectors.toList());
        assertThat(contractors.contains("testEmail")).isTrue();
        assertThat(contractors.contains("testEmail2")).isTrue();
        assertThat(contractors.contains("testEmail3")).isTrue();

    }

    @DisplayName("AgreementSign들을 저장한다.")
    @Test
    void saveAgreementSigns() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user, user2, user3));

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        AgreementSign agreementSign1 = AgreementSign.create(user, contract);
        AgreementSign agreementSign2 = AgreementSign.create(user2, contract);
        AgreementSign agreementSign3 = AgreementSign.create(user3, contract);

        /** when */

        agreementSignServiceSupport.saveAgreementSigns(List.of(agreementSign1, agreementSign2, agreementSign3));

        /** then */

        List<AgreementSign> agreementSigns = agreementSignRepository.findAll();
        assertThat(agreementSigns).hasSize(3);


    }

    @DisplayName("AgreementSign의 상태가 이미 Y이면 IsAlreadySignedException을 던진다.")
    @Test
    void checkMySignStateIsN() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        AgreementSign agreementSign1 = AgreementSign.create(user, contract);
        agreementSign1.sign();
        agreementSignRepository.save(agreementSign1);

        /** when then */

        assertThatThrownBy(() -> agreementSignServiceSupport.checkMySignStateIsN(agreementSign1))
                .isInstanceOf(IsAlreadySignedException.class);
    }

    @DisplayName("AgreementSign 리스트에서 나의 AgreementSign을 반환한다.")
    @Test
    void getMyAgreementSign() {

        /** given */

        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user, user2, user3));

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        AgreementSign agreementSign1 = AgreementSign.create(user, contract);
        AgreementSign agreementSign2 = AgreementSign.create(user2, contract);
        AgreementSign agreementSign3 = AgreementSign.create(user3, contract);
        agreementSignRepository.saveAll(List.of(agreementSign1, agreementSign2, agreementSign3));

        /** when */

        AgreementSign result = agreementSignServiceSupport.getMyAgreementSign(user.getEmail(), List.of(agreementSign1, agreementSign2, agreementSign3));

        /** then */

        assertThat(result.getUser().getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getContract().getContractId()).isEqualTo(contract.getContractId());

    }


    @DisplayName("AgreementSign 리스트에서 나의 AgreementSign이 없으면 IsNotContractParticipantException을 던진다.")
    @Test
    void getMyAgreementSignException() {

        /** given */

        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user, user2, user3));

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        AgreementSign agreementSign1 = AgreementSign.create(user, contract);
        AgreementSign agreementSign2 = AgreementSign.create(user2, contract);
        agreementSignRepository.saveAll(List.of(agreementSign1, agreementSign2));

        /** when then */

        assertThatThrownBy(() -> agreementSignServiceSupport.getMyAgreementSign(user3.getEmail(), List.of(agreementSign1, agreementSign2)))
                .isInstanceOf(IsNotContractParticipantException.class);
    }

    @DisplayName("계약서 참여자들이 모두 서명해서 AgreementSigns들이 모두 Y가 되면 계약서의 상태가 CONCLUDE로 바뀐다.")
    @Test
    void checkIsAllAgree() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user, user2, user3));

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        AgreementSign agreementSign1 = AgreementSign.create(user, contract);
        agreementSign1.sign();
        AgreementSign agreementSign2 = AgreementSign.create(user2, contract);
        agreementSign2.sign();
        AgreementSign agreementSign3 = AgreementSign.create(user3, contract);
        agreementSign3.sign();
        agreementSignRepository.saveAll(List.of(agreementSign1, agreementSign2, agreementSign3));

        contract = contractRepository.findContractWithSignsByContractId(contract.getContractId()).get();

        /** when */

        agreementSignServiceSupport.checkIsAllAgree(contract);

        /** then */

        assertThat(contract.getContractState()).isEqualTo(ContractState.CONCLUDE);


    }

    @DisplayName("AgreementSign들을 지운다.")
    @Test
    void deleteAgreementSigns() {

        /** given */

        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user, user2, user3));

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        AgreementSign agreementSign1 = AgreementSign.create(user, contract);
        AgreementSign agreementSign2 = AgreementSign.create(user2, contract);
        AgreementSign agreementSign3 = AgreementSign.create(user3, contract);
        agreementSignRepository.saveAll(List.of(agreementSign1, agreementSign2, agreementSign3));

        /** when */

        agreementSignServiceSupport.deleteAgreementSigns(List.of(agreementSign1, agreementSign2, agreementSign3));

        /** then */

        List<AgreementSign> agreementSigns = agreementSignRepository.findAll();
        assertThat(agreementSigns).hasSize(0);
    }


}