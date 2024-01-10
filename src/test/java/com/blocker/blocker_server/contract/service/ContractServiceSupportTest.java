package com.blocker.blocker_server.contract.service;

import com.blocker.blocker_server.IntegrationTestSupport;
import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.board.repository.BoardRepository;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.domain.ContractState;
import com.blocker.blocker_server.contract.dto.response.ContractorAndSignState;
import com.blocker.blocker_server.contract.dto.response.GetContractResponseDto;
import com.blocker.blocker_server.contract.exception.*;
import com.blocker.blocker_server.contract.repository.ContractRepository;
import com.blocker.blocker_server.sign.domain.AgreementSign;
import com.blocker.blocker_server.sign.domain.SignState;
import com.blocker.blocker_server.sign.repository.AgreementSignRepository;
import com.blocker.blocker_server.sign.service.AgreementSignServiceSupport;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.repository.UserRepository;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

class ContractServiceSupportTest extends IntegrationTestSupport {

    @Autowired
    private ContractServiceSupport contractServiceSupport;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private AgreementSignRepository agreementSignRepository;
    @Autowired
    private BoardRepository boardRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        boardRepository.deleteAllInBatch();
        agreementSignRepository.deleteAllInBatch();
        contractRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("계약서 인덱스로 계약서를 조회한다.")
    @Test
    void getContractId() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        /** when */

        Contract result = contractServiceSupport.getContractById(contract.getContractId());

        /** then */

        assertThat(result.getContractId()).isEqualTo(contract.getContractId());

    }

    @DisplayName("계약서 인덱스로 조회했을 때 없으면 ContractNotFoundException을 던진다.")
    @Test
    void getBoardByIdThrowNotFoundException() {

        /** given */

        /** when then */
        assertThatThrownBy(() -> contractServiceSupport.getContractById(1l))
                .isInstanceOf(ContractNotFoundException.class);

    }

    @DisplayName("계약서 작성자가 아니면 IsNotContractWriterException을 던진다.")
    @Test
    void checkIsContractWriter() {

        /** given */
        User writer = User.create("writer", "writer", "writer", "writer", List.of("USER"));
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.saveAll(List.of(writer, user));

        Contract contract = Contract.create(writer, "testTitle", "testContent");
        contractRepository.save(contract);

        /** when then */
        assertThatThrownBy(() -> contractServiceSupport.checkIsContractWriter(user.getEmail(), contract))
                .isInstanceOf(IsNotContractWriterException.class);
    }

    @DisplayName("계약서를 저장한다.")
    @Test
    void save() {

        /** given */

        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);

        Contract contract = Contract.create(user, "testTitle", "testContent");

        /** when */

        contractServiceSupport.saveContract(contract);

        /** then */
        List<Contract> contracts = contractRepository.findAll();
        assertThat(contracts).hasSize(1);
        assertThat(contracts.get(0).getContractId()).isEqualTo(contract.getContractId());

    }

    @DisplayName("계약서를 수정할 때 체결된 계약서라면 CannotModifyContractInConcludedStateException를 던진다.")
    @Test
    void checkIsConcludeContractForModify() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contract.updateStateToConclude();
        contractRepository.save(contract);

        /** when then */
        assertThatThrownBy(() -> contractServiceSupport.checkIsConcludeContractForModify(user.getEmail(), contract))
                .isInstanceOf(CannotModifyContractInConcludedStateException.class);

    }

    @DisplayName("계약서의 상태가 진행 중이라면 서명 상태가 모두 N으로 바뀐다.")
    @Test
    void checkIsProceedContractForModify() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));

        userRepository.saveAll(List.of(user, user2));

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contract.updateStateToProceed();
        contractRepository.save(contract);

        AgreementSign agreementSign1 = AgreementSign.create(user, contract);
        agreementSign1.sign();
        AgreementSign agreementSign2 = AgreementSign.create(user2, contract);
        agreementSign2.sign();
        agreementSignRepository.saveAll(List.of(agreementSign1, agreementSign2));

        /** when */
        contractServiceSupport.checkIsProceedContractForModify(contract);

        /** then */
        List<AgreementSign> agreementSigns = agreementSignRepository.findByContract(contract);

        assertThat(agreementSigns.get(0).getSignState()).isEqualTo(SignState.N);
        assertThat(agreementSigns.get(1).getSignState()).isEqualTo(SignState.N);

    }

    @DisplayName("엔티티 리스트를 Dto 리스트로 바꾼다.")
    @Test
    void entityListToDtoList() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);

        Contract contract1 = Contract.create(user, "testTitle", "testContent");
        Contract contract2 = Contract.create(user, "testTitle", "testContent");
        Contract contract3 = Contract.create(user, "testTitle", "testContent");
        contractRepository.saveAll(List.of(contract1, contract2, contract3));

        /** when */

        List<GetContractResponseDto> result = contractServiceSupport.entityListToDtoList(List.of(contract1, contract2, contract3));

        /** then */

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getContractId()).isEqualTo(contract1.getContractId());
        assertThat(result.get(1).getContractId()).isEqualTo(contract2.getContractId());
        assertThat(result.get(2).getContractId()).isEqualTo(contract3.getContractId());

    }

    @DisplayName("미체결 계약서가 아니면 IsNotNotProceedContractException을 던진다.")
    @Test
    void checkIsNotProceedContract() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);

        Contract contract1 = Contract.create(user, "testTitle", "testContent");
        contract1.updateStateToProceed();
        Contract contract2 = Contract.create(user, "testTitle", "testContent");
        contract2.updateStateToConclude();
        contractRepository.saveAll(List.of(contract1, contract2));

        /** when then */

        assertThatThrownBy(() -> contractServiceSupport.checkIsNotProceedContract(contract1)).isInstanceOf(IsNotNotProceedContractException.class);
        assertThatThrownBy(() -> contractServiceSupport.checkIsNotProceedContract(contract2)).isInstanceOf(IsNotNotProceedContractException.class);
    }

    @DisplayName("계약서 인덱스로 계약서를 삭제한다.")
    @Test
    void deleteContractById() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        /** when */

        contractServiceSupport.deleteContractById(contract.getContractId());

        /** then */

        List<Contract> contracts = contractRepository.findAll();
        assertThat(contracts).hasSize(0);

    }

    @DisplayName("해당 계약서가 포함된 게시글이 있으면 ExistBoardsBelongingToContractException을 던진다.")
    @Test
    void checkExistsBoardBelongingToContract() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        Board board = Board.create(user, "testTitle", "testContent", "testImage", "testInfo", contract);
        boardRepository.save(board);

        /** when then */

        assertThatThrownBy(() -> contractServiceSupport.checkExistsBoardBelongingToContract(contract))
                .isInstanceOf(ExistBoardsBelongingToContractException.class);

    }

    @DisplayName("진행 중 계약서가 아니면 IsNotProceedContractException을 던진다.")
    @Test
    void checkIsProceedContract() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        /** when then */

        assertThatThrownBy(() -> contractServiceSupport.checkIsProceedContract(contract))
                .isInstanceOf(IsNotProceedContractException.class);
    }

    @DisplayName("계약 참여자가 아니면 IsNotParticipantException을 던진다.")
    @Test
    void checkIsParticipant() {

        /** given */
        User user1 = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2, user3));

        Contract contract = Contract.create(user1, "testTitle", "testContent");
        contract.updateStateToProceed();
        contractRepository.save(contract);

        AgreementSign agreementSign1 = AgreementSign.create(user1, contract);
        AgreementSign agreementSign2 = AgreementSign.create(user2, contract);
        agreementSignRepository.saveAll(List.of(agreementSign1, agreementSign2));

        /** when then */

        assertThatThrownBy(() -> contractServiceSupport.checkIsParticipant(user3, contract))
                .isInstanceOf(IsNotParticipantException.class);

    }

    @DisplayName("진행 중 계약서의 Sign들도 fetch join으로 같이 조회한다.")
    @Test
    void getContractWIthSignsById() {

        /** given */
        User user1 = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2));

        Contract contract = Contract.create(user1, "testTitle", "testContent");
        contract.updateStateToProceed();
        contractRepository.save(contract);

        AgreementSign agreementSign1 = AgreementSign.create(user1, contract);
        AgreementSign agreementSign2 = AgreementSign.create(user2, contract);
        agreementSignRepository.saveAll(List.of(agreementSign1, agreementSign2));

        /** when */

        Contract result = contractServiceSupport.getContractWIthSignsById(contract.getContractId());

        /** then */

        assertThat(result.getAgreementSigns()).hasSize(2);
        assertThat(Hibernate.isInitialized(result.getAgreementSigns())).isTrue();
        assertThat(Hibernate.isInitialized(result.getAgreementSigns().get(0).getUser())).isTrue();
        assertThat(Hibernate.isInitialized(result.getAgreementSigns().get(1).getUser())).isTrue();

    }

    @DisplayName("AgreementSigns List를 Dto로 바꾼다.")
    @Test
    void getContractorAndSignState() {

        /** given */
        User user1 = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2));

        Contract contract = Contract.create(user1, "testTitle", "testContent");
        contract.updateStateToProceed();
        contractRepository.save(contract);

        AgreementSign agreementSign1 = AgreementSign.create(user1, contract);
        AgreementSign agreementSign2 = AgreementSign.create(user2, contract);
        agreementSignRepository.saveAll(List.of(agreementSign1, agreementSign2));

        /** when */

        List<AgreementSign> result = agreementSignRepository.findByContract(contract);

        /** then */
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUser().getEmail()).isEqualTo("testEmail");
        assertThat(result.get(1).getUser().getEmail()).isEqualTo("testEmail2");

    }

    @DisplayName("체결 계약서가 아니라면 IsNotConcludeContractException을 던진다.")
    @Test
    void checkIsConcludeContract() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        /** when then */
        assertThatThrownBy(() -> contractServiceSupport.checkIsConcludeContract(contract))
                .isInstanceOf(IsNotConcludeContractException.class);

    }

}