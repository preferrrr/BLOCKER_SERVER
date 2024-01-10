package com.blocker.blocker_server.contract.repository;

import com.blocker.blocker_server.contract.domain.CancelContract;
import com.blocker.blocker_server.contract.domain.CancelContractState;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.dto.response.GetCancelContractResponseDto;
import com.blocker.blocker_server.contract.service.CancelContractService;
import com.blocker.blocker_server.sign.domain.CancelSign;
import com.blocker.blocker_server.sign.repository.AgreementSignRepository;
import com.blocker.blocker_server.sign.repository.CancelSignRepository;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CancelContractRepositoryTest {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CancelSignRepository cancelSignRepository;

    @Autowired
    private CancelContractRepository cancelContractRepository;


    @DisplayName("")
    @Test
    void getCancelContractTest() {

        /** given */

        User user1 = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2));

        Contract contract1 = Contract.create(user1, "testTitle", "testContent");
        Contract contract2 = Contract.create(user2, "testTitle", "testContent");
        contract1.updateStateToConclude();
        contract2.updateStateToConclude();
        contractRepository.saveAll(List.of(contract1, contract2));


        CancelContract cancelContract1 = CancelContract.create(user1, contract1, "testTitle", "testContent");
        CancelContract cancelContract2 = CancelContract.create(user1, contract2, "testTitle", "testContent");
        cancelContractRepository.saveAll(List.of(cancelContract1, cancelContract2));

        CancelSign cancelSign1 = CancelSign.create(user1, cancelContract1);
        CancelSign cancelSign2 = CancelSign.create(user2, cancelContract1);
        CancelSign cancelSign3 = CancelSign.create(user1, cancelContract2);
        CancelSign cancelSign4 = CancelSign.create(user2, cancelContract2);
        cancelSignRepository.saveAll(List.of(cancelSign1, cancelSign2, cancelSign3, cancelSign4));

        /** when */

        List<CancelContract> response1 =  cancelContractRepository.findCancelContractsByUserAndState(user1, CancelContractState.CANCELING);
        List<CancelContract> response2 =  cancelContractRepository.findCancelContractsByUserAndState(user2, CancelContractState.CANCELING);


        /** then */
        assertThat(response1).hasSize(2);
        assertThat(response1.size()).isEqualTo(response2.size());

    }

}