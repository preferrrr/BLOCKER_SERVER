package com.blocker.blocker_server.sign.service;

import com.blocker.blocker_server.contract.domain.CancelContract;
import com.blocker.blocker_server.sign.domain.AgreementSign;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.domain.ContractState;
import com.blocker.blocker_server.commons.exception.*;import com.blocker.blocker_server.sign.repository.AgreementSignRepository;
import com.blocker.blocker_server.contract.repository.CancelContractRepository;
import com.blocker.blocker_server.sign.repository.CancelSignRepository;
import com.blocker.blocker_server.contract.repository.ContractRepository;
import com.blocker.blocker_server.sign.domain.CancelSign;
import com.blocker.blocker_server.sign.domain.SignState;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CancelSignService {

    private final CancelSignRepository cancelSignRepository;
    private final ContractRepository contractRepository;
    private final AgreementSignRepository agreementSignRepository;
    private final CancelContractRepository cancelContractRepository;

    @Transactional
    public void cancelContract(User me, Long contractId) {

        Contract contract = contractRepository.findById(contractId).orElseThrow(()->new NotFoundException("[cancel contract] contractId : " + contractId));

        //계약서의 상태가 체결이 아니면 반환
        if(!contract.getContractState().equals(ContractState.CONCLUDE))
            throw new NotConcludeContractException("email, contractId : " + me.getEmail() + ", " + contractId);

        List<AgreementSign> agreementSigns = agreementSignRepository.findByContract(contract);
        //계약의 참여자가 아니면 403 반환
        if(agreementSigns.stream().noneMatch(agreementSign -> agreementSign.getUser().getEmail().equals(me.getEmail())))
            throw new ForbiddenException("[cancel contract] email, contractId : " + me.getEmail() + ", " + contractId);

        //이미 파기 계약 진행 중이라면 409 반환
        if(cancelContractRepository.existsByContract(contract))
            throw new ExistsCancelSignException("email, contractId : " + me.getEmail() + ", " + contractId);

        CancelContract cancelContract = CancelContract.create(me, contract, contract.getTitle(), contract.getContent());

        List<CancelSign> cancelSigns = agreementSigns.stream()
                .map(agreementSign -> CancelSign.create(agreementSign.getUser(), cancelContract))
                .collect(Collectors.toList());

        cancelContractRepository.save(cancelContract);
        cancelSignRepository.saveAll(cancelSigns);
    }

    @Transactional
    public void signCancelContract(User me, Long cancelContractId) {

        List<CancelSign> cancelSigns = cancelSignRepository.findByCancelContract(cancelContractRepository.getReferenceById(cancelContractId));

        if (cancelSigns.size() < 2)
            throw new NotFoundException("[sign contract] email, contractId : " + me.getEmail() + ", " + cancelContractId);

        // 계약 참여자들 모두 가져오고, 나의 Sign에 서명 후
        // Sign 모두 Y가 되면 블록체인으로 계약 체결, 계약서 상태 CANCELED로 바꿈.
        sign(cancelSigns, me.getEmail(), cancelContractId);

        isAllAgree(cancelSigns, me.getEmail(), cancelContractId);

    }

    private void isAllAgree(List<CancelSign> cancelSigns, String myEmail, Long cancelContractId) {
        boolean allY = cancelSigns.stream()
                .allMatch(sign -> sign.getSignState().equals(SignState.Y));

        if (allY) {
            CancelContract cancelContract = cancelContractRepository.findById(cancelContractId).orElseThrow(() -> new NotFoundException("[sign cancel contract] email, cancelContractId : " + myEmail + ", " + cancelContractId));

            cancelContract.updateStateToCanceled();

            //TODO: 블록체인으로 계약 체결되도록

        }
    }

    private void sign(List<CancelSign> cancelSigns, String myEmail, Long cancelContractId) {

        // 계약 참여자들 모두 가져오고, 나의 Sign에 서명 후
        // Sign 모두 Y가 되면 블록체인으로 계약 체결, 계약서 상태 CONCLUDE로 바꿈.

        CancelSign myCancelSign = cancelSigns.stream() // signs 중 내 sign 찾기
                .filter(sign -> sign.getUser().getEmail().equals(myEmail))
                .findFirst().orElseThrow(() -> new NotFoundException("[sign contract] email, contractId : " + myEmail + ", " + cancelContractId));

        //이미 서명 했으면 409 응답
        if (myCancelSign.getSignState().equals(SignState.Y))
            throw new DuplicateSignException("cancel contractId, email : " + cancelContractId + ", " + myEmail);

        myCancelSign.sign();

    }
}
