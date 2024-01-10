package com.blocker.blocker_server.sign.service;

import com.blocker.blocker_server.commons.exception.DuplicateSignException;
import com.blocker.blocker_server.commons.exception.ForbiddenException;
import com.blocker.blocker_server.commons.exception.NotFoundException;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgreementSignServiceSupport {

    private final AgreementSignRepository agreementSignRepository;
    private final UserRepository userRepository;

    @Transactional
    public void modifySignsToN(Contract contract) {

        List<AgreementSign> agreementSignList = agreementSignRepository.findByContract(contract);

        agreementSignList.stream()
                .filter(sign -> sign.getSignState().equals(SignState.Y))
                .forEach(sign -> sign.cancel());
    }

    public void checkIsNotProceedContract(Contract contract) {
        if (!contract.getContractState().equals(ContractState.NOT_PROCEED))
            throw new IsNotNotProceedContractException("contract id: " + contract.getContractId());
    }

    public void checkIsEmptyContractor(List<String> contractors) {
        if (contractors.isEmpty())
            throw new EmptyParticipantException();
    }

    public List<AgreementSign> createAgreementSigns(Contract contract, User me, List<String> contractors) {
        List<AgreementSign> agreementSigns = contractors.stream() // 계약에 참여하는 사람들
                .map(email -> userRepository.findByEmail(email)
                        .orElseThrow(() -> new UserNotFoundException("email: " + email)))
                .map(contractor -> AgreementSign.create(contractor, contract))
                .collect(Collectors.toList());
        agreementSigns.add(AgreementSign.create(me, contract)); // 나도 계약 참여자. 나도 나중에 서명해야함.

        return agreementSigns;
    }

    @Transactional
    public void saveAgreementSigns(List<AgreementSign> agreementSigns) {
        agreementSignRepository.saveAll(agreementSigns);
    }


    public void checkMySignStateIsN(AgreementSign agreementSign) {
        if (agreementSign.getSignState().equals(SignState.Y))
            throw new IsAlreadySignedException("email: " + agreementSign.getUser().getEmail());
    }

    public AgreementSign getMyAgreementSign(String email, List<AgreementSign> agreementSigns) {
        return agreementSigns.stream()
                .filter(agreementSign -> agreementSign.getUser().getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new IsNotContractParticipantException("email: " + email));
    }

    @Transactional
    public void checkIsAllAgree(Contract contract) {
        boolean isAllY = contract.getAgreementSigns().stream()
                .allMatch(agreementSign -> agreementSign.getSignState().equals(SignState.Y));

        if (isAllY) {
            contract.updateStateToConclude();

            //TODO : 블록체인으로 체결되도록.
        }
    }

    public void deleteAgreementSigns(List<AgreementSign> agreementSigns) {
        agreementSignRepository.deleteAllInBatch(agreementSigns);
    }
}
