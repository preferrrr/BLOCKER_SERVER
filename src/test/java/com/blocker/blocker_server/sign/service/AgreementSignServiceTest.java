package com.blocker.blocker_server.sign.service;

import com.blocker.blocker_server.IntegrationTestSupport;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.domain.ContractState;
import com.blocker.blocker_server.contract.repository.ContractRepository;
import com.blocker.blocker_server.sign.domain.AgreementSign;
import com.blocker.blocker_server.sign.domain.SignState;
import com.blocker.blocker_server.sign.dto.request.ProceedSignRequestDto;
import com.blocker.blocker_server.sign.repository.AgreementSignRepository;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class AgreementSignServiceTest extends IntegrationTestSupport {

    @Autowired
    private AgreementSignService agreementSignService;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AgreementSignRepository agreementSignRepository;

    @DisplayName("계약을 진행하면, 계약서의 상태가 PROCEED로 바뀌고 AgreementSign이 저장되며 SignState는 모두 N이다.")
    @Test
    void proceedContract() {

        /** given */

        User user1 = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2, user3));

        Contract contract = Contract.create(user1, "testTitle", "testContent");
        contractRepository.save(contract);

        ProceedSignRequestDto dto = ProceedSignRequestDto.builder()
                .contractId(contract.getContractId())
                .contractors(List.of(user2.getEmail(), user3.getEmail()))
                .build();


        /** when */

        agreementSignService.proceedContract(user1, dto);

        /** then */

        assertThat(contract.getContractState()).isEqualTo(ContractState.PROCEED);

        List<AgreementSign> agreementSigns = agreementSignRepository.findByContract(contract);
        assertThat(agreementSigns).hasSize(3);
        assertThat(agreementSigns).allMatch(agreementSign -> agreementSign.getSignState().equals(SignState.N));

    }

    @DisplayName("계약서에 서명하면 AgreementSign의 SignState가 Y로 바뀐다.")
    @Test
    void signContract() {

        /** given */
        User user1 = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2, user3));

        Contract contract = Contract.create(user1, "testTitle", "testContent");
        contract.updateStateToProceed();
        contractRepository.save(contract);

        AgreementSign agreementSign1 = AgreementSign.create(user1, contract);
        AgreementSign agreementSign2 = AgreementSign.create(user2, contract);
        AgreementSign agreementSign3 = AgreementSign.create(user3, contract);
        agreementSignRepository.saveAll(List.of(agreementSign1, agreementSign2, agreementSign3));


        /** when */

        agreementSignService.signContract(user1, contract.getContractId());

        /** then */

        AgreementSign mySign = agreementSignRepository.findByContractAndUser(contract, user1).get();
        assertThat(mySign.getSignState()).isEqualTo(SignState.Y);


    }

    @DisplayName("진행 중 계약서를 취소하면, AgreementSign은 모두 지워지고 ContractState는 NOT_PROCEED로 바뀐다.")
    @Test
    void test() {

        /** given */
        User user1 = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2, user3));

        Contract contract = Contract.create(user1, "testTitle", "testContent");
        contract.updateStateToProceed();
        contractRepository.save(contract);

        AgreementSign agreementSign1 = AgreementSign.create(user1, contract);
        AgreementSign agreementSign2 = AgreementSign.create(user2, contract);
        AgreementSign agreementSign3 = AgreementSign.create(user3, contract);
        agreementSignRepository.saveAll(List.of(agreementSign1, agreementSign2, agreementSign3));

        /** when */

        agreementSignService.breakContract(user1, contract.getContractId());

        /** then */

        assertThat(contract.getContractState()).isEqualTo(ContractState.NOT_PROCEED);

        List<AgreementSign> agreementSigns = agreementSignRepository.findByContract(contract);
        assertThat(agreementSigns).isEmpty();
    }
}