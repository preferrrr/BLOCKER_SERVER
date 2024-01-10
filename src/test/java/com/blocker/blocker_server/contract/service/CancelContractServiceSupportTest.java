package com.blocker.blocker_server.contract.service;

import com.blocker.blocker_server.IntegrationTestSupport;
import com.blocker.blocker_server.contract.domain.CancelContract;
import com.blocker.blocker_server.contract.domain.CancelContractState;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.dto.response.CancelContractorAndSignState;
import com.blocker.blocker_server.contract.dto.response.GetCancelContractResponseDto;
import com.blocker.blocker_server.contract.exception.CancelContractNotFoundException;
import com.blocker.blocker_server.contract.exception.IsNotCancelContractParticipant;
import com.blocker.blocker_server.contract.exception.IsNotCanceledCancelContract;
import com.blocker.blocker_server.contract.exception.IsNotCancelingCancelContract;
import com.blocker.blocker_server.contract.repository.CancelContractRepository;
import com.blocker.blocker_server.contract.repository.ContractRepository;
import com.blocker.blocker_server.sign.domain.CancelSign;
import com.blocker.blocker_server.sign.repository.CancelSignRepository;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.repository.UserRepository;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

class CancelContractServiceSupportTest extends IntegrationTestSupport {

    @Autowired
    private CancelContractServiceSupport cancelContractServiceSupport;
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
        contractRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }



    @DisplayName("인덱스로 파기 계약서를 조회하고, fetch join으로 파기 서명과 파기 서명 유저를 같이 조회한다.")
    @Test
    void getCancelContractWithSignsById() {

        /** given */
        User user1 = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2));

        Contract contract1 = Contract.create(user1, "testTitle1", "testContent1");
        contractRepository.save(contract1);

        CancelContract cancelContract1 = CancelContract.create(user1, contract1, "testTitle1", "testContent1");
        cancelContractRepository.save(cancelContract1);

        CancelSign cancelSign1 = CancelSign.create(user1, cancelContract1);
        CancelSign cancelSign2 = CancelSign.create(user2, cancelContract1);
        cancelSignRepository.saveAll(List.of(cancelSign1, cancelSign2));

        /** when */

        CancelContract result = cancelContractServiceSupport.getCancelContractWithSignsById(cancelContract1.getCancelContractId());

        /** then */

        assertThat(result.getCancelSigns()).hasSize(2);

        assertThat(Hibernate.isInitialized(result.getCancelSigns())).isTrue();
        assertThat(Hibernate.isInitialized(result.getCancelSigns().get(0).getUser())).isTrue();

        List<String> names = result.getCancelSigns().stream()
                .map(sign -> sign.getUser().getName())
                .collect(Collectors.toList());

        assertThat(names.contains(user1.getName()));
        assertThat(names.contains(user2.getName()));

    }

    @DisplayName("User와 CancelContractState로 파기 계약서를 조회한다.")
    @Test
    void getCancelContractsByUserAndState() {

        /** given */
        User user1 = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2));

        Contract contract1 = Contract.create(user1, "testTitle1", "testContent1");
        Contract contract2 = Contract.create(user1, "testTitle2", "testContent2");
        contractRepository.saveAll(List.of(contract1, contract2));

        CancelContract cancelContract1 = CancelContract.create(user1, contract1, "testTitle1", "testContent1");
        CancelContract cancelContract2 = CancelContract.create(user2, contract2, "testTitle2", "testContent2");
        cancelContractRepository.saveAll(List.of(cancelContract1, cancelContract2));

        CancelSign cancelSign1 = CancelSign.create(user1, cancelContract1);
        CancelSign cancelSign2 = CancelSign.create(user2, cancelContract1);
        CancelSign cancelSign3 = CancelSign.create(user1, cancelContract2);
        CancelSign cancelSign4 = CancelSign.create(user2, cancelContract2);
        cancelSignRepository.saveAll(List.of(cancelSign1, cancelSign2, cancelSign3, cancelSign4));


        /** when */

        List<CancelContract> user1Result = cancelContractServiceSupport.getCancelContractsByUserAndState(user1, CancelContractState.CANCELING);
        List<CancelContract> user2Result = cancelContractServiceSupport.getCancelContractsByUserAndState(user2, CancelContractState.CANCELING);

        /** then */

        assertThat(user1Result).hasSize(2);
        assertThat(user2Result).hasSize(2);

    }

    @DisplayName("파기 계약서 리스트를 dto로 변환한다.")
    @Test
    void entityListToDtoList() {

        /** given */
        User user1 = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2));

        Contract contract1 = Contract.create(user1, "testTitle1", "testContent1");
        Contract contract2 = Contract.create(user1, "testTitle2", "testContent2");
        Contract contract3 = Contract.create(user1, "testTitle3", "testContent3");
        contractRepository.saveAll(List.of(contract1, contract2 ,contract3));

        CancelContract cancelContract1 = CancelContract.create(user1, contract1, "testTitle1", "testContent1");
        CancelContract cancelContract2 = CancelContract.create(user1, contract2, "testTitle2", "testContent2");
        CancelContract cancelContract3 = CancelContract.create(user1, contract3, "testTitle3", "testContent3");
        cancelContractRepository.saveAll(List.of(cancelContract1, cancelContract2, cancelContract3));

        List<CancelContract> cancelContracts = cancelContractRepository.findAll();

        /** when */

        List<GetCancelContractResponseDto> result = cancelContractServiceSupport.entityListToDtoList(cancelContracts);

        /** then */
        assertThat(result).hasSize(3);

        List<Long> ids = result.stream()
                .map(cancelContract -> cancelContract.getCancelContractId())
                .collect(Collectors.toList());
        assertThat(ids.contains(cancelContract1.getCancelContractId())).isTrue();
        assertThat(ids.contains(cancelContract2.getCancelContractId())).isTrue();
        assertThat(ids.contains(cancelContract3.getCancelContractId())).isTrue();
    }

    @DisplayName("계약자와 서명 상태 dto 리스트를 반환한다.")
    @Test
    void getCancelContractorAndSignState() {

        /** given */
        User user1 = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2, user3));

        Contract contract1 = Contract.create(user1, "testTitle1", "testContent1");
        contractRepository.save(contract1);

        CancelContract cancelContract1 = CancelContract.create(user1, contract1, "testTitle1", "testContent1");
        cancelContractRepository.save(cancelContract1);

        CancelSign cancelSign1 = CancelSign.create(user1, cancelContract1);
        CancelSign cancelSign2 = CancelSign.create(user2, cancelContract1);
        CancelSign cancelSign3 = CancelSign.create(user3, cancelContract1);
        cancelSignRepository.saveAll(List.of(cancelSign1, cancelSign2, cancelSign3));

        CancelContract cancelContract = cancelContractServiceSupport.getCancelContractWithSignsById(cancelContract1.getCancelContractId());

        /** when */

        List<CancelContractorAndSignState> result = cancelContractServiceSupport.getCancelContractorAndSignState(cancelContract.getCancelSigns());

        /** then */

        assertThat(result.size()).isEqualTo(cancelContract.getCancelSigns().size());

        List<String> users = result.stream()
                .map(sign -> sign.getContractor())
                .collect(Collectors.toList());

        assertThat(users.contains(user1.getName())).isTrue();
        assertThat(users.contains(user2.getName())).isTrue();
        assertThat(users.contains(user3.getName())).isTrue();

    }

    @DisplayName("파기 진행 중 계약서가 아니면 IsNotCancelingCancelContract을 던진다.")
    @Test
    void checkIsCancelingCancelContract() {

        /** given */
        User user1 = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2, user3));

        Contract contract1 = Contract.create(user1, "testTitle1", "testContent1");
        contractRepository.save(contract1);

        CancelContract cancelContract1 = CancelContract.create(user1, contract1, "testTitle1", "testContent1");
        cancelContract1.updateStateToCanceled();
        cancelContractRepository.save(cancelContract1);


        /** when then */

        assertThatThrownBy(() -> cancelContractServiceSupport.checkIsCancelingCancelContract(cancelContract1))
                .isInstanceOf(IsNotCancelingCancelContract.class);
    }

    @DisplayName("파기 체결된 계약서가 아니면 IsNotCancelingCancelContract을 던진다.")
    @Test
    void checkIsCanceledCancelContract() {

        /** given */
        User user1 = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2, user3));

        Contract contract1 = Contract.create(user1, "testTitle1", "testContent1");
        contractRepository.save(contract1);

        CancelContract cancelContract1 = CancelContract.create(user1, contract1, "testTitle1", "testContent1");
        cancelContractRepository.save(cancelContract1);


        /** when then */

        assertThatThrownBy(() -> cancelContractServiceSupport.checkIsCanceledCancelContract(cancelContract1))
                .isInstanceOf(IsNotCanceledCancelContract.class);
    }

    @DisplayName("파기 계약 참여자가 아니면 IsNotCancelContractParticipant을 던진다.")
    @Test
    void test() {

        /** given */
        User user1 = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2, user3));

        Contract contract1 = Contract.create(user1, "testTitle1", "testContent1");
        contractRepository.save(contract1);

        CancelContract cancelContract1 = CancelContract.create(user1, contract1, "testTitle1", "testContent1");
        cancelContractRepository.save(cancelContract1);

        CancelSign cancelSign1 = CancelSign.create(user1, cancelContract1);
        CancelSign cancelSign2 = CancelSign.create(user2, cancelContract1);
        cancelSignRepository.saveAll(List.of(cancelSign1, cancelSign2));


        /** when then */

        assertThatThrownBy(() -> cancelContractServiceSupport.checkIsCancelContractParticipant(user3, cancelContract1))
                .isInstanceOf(IsNotCancelContractParticipant.class);

    }


}