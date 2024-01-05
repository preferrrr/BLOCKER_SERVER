package com.blocker.blocker_server.sign.service;

import com.blocker.blocker_server.IntegrationTestSupport;
import com.blocker.blocker_server.contract.domain.CancelContract;
import com.blocker.blocker_server.contract.domain.CancelContractState;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.domain.ContractState;
import com.blocker.blocker_server.contract.repository.CancelContractRepository;
import com.blocker.blocker_server.contract.repository.ContractRepository;
import com.blocker.blocker_server.sign.domain.AgreementSign;
import com.blocker.blocker_server.sign.domain.CancelSign;
import com.blocker.blocker_server.sign.domain.SignState;
import com.blocker.blocker_server.sign.repository.AgreementSignRepository;
import com.blocker.blocker_server.sign.repository.CancelSignRepository;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Transactional
class CancelSignServiceTest extends IntegrationTestSupport {

    @Autowired
    private CancelSignService cancelSignService;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CancelSignRepository cancelSignRepository;

    @Autowired
    private AgreementSignRepository agreementSignRepository;

    @Autowired
    private CancelContractRepository cancelContractRepository;

    @DisplayName("체결된 계약서를 파기한다.")
    @Test
    void cancelContract() {

        /** given */
        User user1 = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2, user3));

        Contract contract = Contract.create(user1, "testTitle", "testContent");
        contract.updateStateToConclude();
        contractRepository.save(contract);

        AgreementSign agreementSign1 = AgreementSign.create(user1, contract);
        AgreementSign agreementSign2 = AgreementSign.create(user2, contract);
        AgreementSign agreementSign3 = AgreementSign.create(user3, contract);
        List.of(agreementSign1, agreementSign2, agreementSign3).stream()
                .forEach(agreementSign -> agreementSign.sign());
        agreementSignRepository.saveAll(List.of(agreementSign1, agreementSign2, agreementSign3));

        /** when */

        cancelSignService.cancelContract(user1, contract.getContractId());

        /** then */

        CancelContract cancelContract = cancelContractRepository.findByContract(contract);
        assertThat(cancelContract).isNotNull();
        assertThat(cancelContract.getCancelContractState()).isEqualTo(CancelContractState.CANCELING);

        List<CancelSign> cancelSigns = cancelSignRepository.findByCancelContract(cancelContract);
        assertThat(cancelSigns).hasSize(3);

    }

    @DisplayName("계약 파기에 동의하면 CancelSign의 SignState가 Y로 바뀐다.")
    @Test
    void signCancelContract() {

        /** given */
        User user1 = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2, user3));

        Contract contract = Contract.create(user1, "testTitle", "testContent");
        contract.updateStateToConclude();
        contractRepository.save(contract);

        CancelContract cancelContract = CancelContract.create(user1, contract, "testTitle", "testContent");
        cancelContractRepository.save(cancelContract);

        CancelSign cancelSign1 = CancelSign.create(user1, cancelContract);
        CancelSign cancelSign2 = CancelSign.create(user2, cancelContract);
        CancelSign cancelSign3 = CancelSign.create(user3, cancelContract);
        cancelSignRepository.saveAll(List.of(cancelSign1, cancelSign2, cancelSign3));


        /** when */

        cancelSignService.signCancelContract(user1, cancelContract.getCancelContractId());

        /** then */

        CancelSign myCancelSign = cancelSignRepository.findByUserAndCancelContract(user1, cancelContract);
        assertThat(myCancelSign.getSignState()).isEqualTo(SignState.Y);

    }
}