package com.blocker.blocker_server.sign.service;

import com.blocker.blocker_server.contract.domain.CancelContract;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.domain.ContractState;
import com.blocker.blocker_server.contract.repository.CancelContractRepository;
import com.blocker.blocker_server.sign.domain.AgreementSign;
import com.blocker.blocker_server.sign.domain.CancelSign;
import com.blocker.blocker_server.sign.domain.SignState;
import com.blocker.blocker_server.sign.exception.*;
import com.blocker.blocker_server.sign.repository.AgreementSignRepository;
import com.blocker.blocker_server.sign.repository.CancelSignRepository;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CancelSignServiceSupport {

    private final AgreementSignRepository agreementSignRepository;
    private final CancelContractRepository cancelContractRepository;
    private final CancelSignRepository cancelSignRepository;

    public void checkIsConcludeContract(Contract contract) {
        if (!contract.getContractState().equals(ContractState.CONCLUDE))
            throw new IsNotConcludeContractForCancelException();
    }

    public void checkIsParticipantForCancel(User user, Contract contract) {
        if (!agreementSignRepository.existsByUserAndContract(user, contract))
            throw new IsNotContractParticipantForCancelException();
    }

    public void checkIsCancelingContract(Contract contract) {
        if (cancelContractRepository.existsByContract(contract))
            throw new IsAlreadyCancelingException();
    }

    public List<CancelSign> createCancelSigns(CancelContract cancelContract, List<AgreementSign> agreementSigns) {
        return agreementSigns.stream()
                .map(agreementSign -> CancelSign.create(agreementSign.getUser(), cancelContract))
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveCancelContract(CancelContract cancelContract) {
        cancelContractRepository.save(cancelContract);
    }

    @Transactional
    public void saveCancelSigns(List<CancelSign> cancelSigns) {
        cancelSignRepository.saveAll(cancelSigns);
    }

    public CancelSign getMyCancelSign(String email, List<CancelSign> cancelSigns) {
        return cancelSigns.stream()
                .filter(cancelSign -> cancelSign.getUser().getEmail().equals(email))
                .findFirst()
                .orElseThrow(IsNotCancelContractParticipantException::new);
    }

    public void checkMySignStateIsN(CancelSign myCancelSign) {
        if (myCancelSign.getSignState().equals(SignState.Y))
            throw new IsAlreadyCancelSignException();
    }

    @Transactional
    public void checkIsAllAgree(CancelContract cancelContract) {
        boolean allY = cancelContract.getCancelSigns().stream()
                .allMatch(sign -> sign.getSignState().equals(SignState.Y));

        if (allY) {
            cancelContract.updateStateToCanceled();

            //TODO: 블록체인으로 계약 체결되도록

        }
    }

}
