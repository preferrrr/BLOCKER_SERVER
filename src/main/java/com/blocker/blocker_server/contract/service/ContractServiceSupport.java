package com.blocker.blocker_server.contract.service;

import com.blocker.blocker_server.board.repository.BoardRepository;
import com.blocker.blocker_server.contract.exception.IsNotProceedContractException;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.domain.ContractState;
import com.blocker.blocker_server.contract.dto.response.ContractorAndSignState;
import com.blocker.blocker_server.contract.dto.response.GetContractResponseDto;
import com.blocker.blocker_server.contract.exception.*;
import com.blocker.blocker_server.contract.repository.ContractRepository;
import com.blocker.blocker_server.sign.domain.AgreementSign;
import com.blocker.blocker_server.sign.repository.AgreementSignRepository;
import com.blocker.blocker_server.sign.service.AgreementSignServiceSupport;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ContractServiceSupport {

    private final ContractRepository contractRepository;
    private final BoardRepository boardRepository;
    private final AgreementSignRepository agreementSignRepository;
    private final AgreementSignServiceSupport agreementSignServiceSupport;

    public Contract getContractById(Long contractId) {
        return contractRepository.findById(contractId).orElseThrow(ContractNotFoundException::new);
    }

    public void checkIsContractWriter(String me, Contract contract) {
        if (!contract.getUser().getEmail().equals(me))
            throw new IsNotContractWriterException();
    }

    @Transactional
    public void saveContract(Contract contract) {
        contractRepository.save(contract);
    }

    public void checkIsConcludeContractForModify(String user, Contract contract) {
        if (contract.getContractState().equals(ContractState.CONCLUDE))
            throw new CannotModifyContractInConcludedStateException();
    }

    @Transactional
    public void checkIsProceedContractForModify(Contract contract) {
        if (contract.getContractState().equals(ContractState.PROCEED))
            agreementSignServiceSupport.modifySignsToN(contract);
    }

    public List<Contract> getContractsByUserAndState(String email, ContractState state) {
        if (state.equals(ContractState.NOT_PROCEED))
            return contractRepository.findNotProceedContractsByUserEmailAndContractState(email, state);
        else
            return contractRepository.findProceedOrConcludeContractListByUserEmailAndContractState(email, state);
    }

    public List<GetContractResponseDto> entityListToDtoList(List<Contract> contracts) {
        return contracts.stream()
                .map(contract -> GetContractResponseDto.of(contract))
                .collect(Collectors.toList());

    }

    public void checkIsNotProceedContract(Contract contract) {
        if (!contract.getContractState().equals(ContractState.NOT_PROCEED))
            throw new IsNotNotProceedContractException();
    }

    @Transactional
    public void deleteContractById(Long contractId) {
        contractRepository.deleteById(contractId);
    }

    public void checkExistsBoardBelongingToContract(Contract contract) {
        if (boardRepository.existsByContract(contract))
            throw new ExistBoardsBelongingToContractException();
    }

    public void checkIsProceedContract(Contract contract) {
        if (!contract.getContractState().equals(ContractState.PROCEED))
            throw new IsNotProceedContractException();
    }

    public void checkIsParticipant(User user, Contract contract) {
        if (!agreementSignRepository.existsByUserAndContract(user, contract))
            throw new IsNotContractParticipantException();
    }

    public Contract getContractWIthSignsById(Long contractId) {
        return contractRepository.findContractWithSignsByContractId(contractId).orElseThrow(ContractNotFoundException::new);
    }

    public List<ContractorAndSignState> getContractorAndSignState(List<AgreementSign> agreementSigns) {
        return agreementSigns.stream()
                .map(sign -> ContractorAndSignState.of(sign.getUser().getName(), sign.getSignState()))
                .collect(Collectors.toList());
    }

    public void checkIsConcludeContract(Contract contract) {
        if (!contract.getContractState().equals(ContractState.CONCLUDE))
            throw new IsNotConcludeContractException();
    }
}
