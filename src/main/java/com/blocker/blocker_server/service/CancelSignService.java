package com.blocker.blocker_server.service;

import com.blocker.blocker_server.entity.*;
import com.blocker.blocker_server.exception.*;
import com.blocker.blocker_server.repository.AgreementSignRepository;
import com.blocker.blocker_server.repository.CancelContractRepository;
import com.blocker.blocker_server.repository.CancelSignRepository;
import com.blocker.blocker_server.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CancelSignService {

    private final CancelSignRepository cancelSignRepository;
    private final ContractRepository contractRepository;
    private final AgreementSignRepository agreementSignRepository;
    private final CancelContractRepository cancelContractRepository;

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

        CancelContract cancelContract = CancelContract.builder()
                .title(contract.getTitle())
                .content(contract.getTitle())
                .user(me)
                .contract(contract)
                .build();

        List<CancelSign> cancelSigns = new ArrayList<>();
        agreementSigns.stream().forEach(
                agreementSign -> cancelSigns.add(CancelSign.builder()
                        .user(agreementSign.getUser())
                        .cancelContract(cancelContract)
                        .build()));

        cancelContractRepository.save(cancelContract);
        cancelSignRepository.saveAll(cancelSigns);
    }

    public void signCancelContract(User me, Long cancelContractId) {

        List<CancelSign> cancelSigns = cancelSignRepository.findByCancelContract(cancelContractRepository.getReferenceById(cancelContractId));

        if (cancelSigns.size() < 2)
            throw new NotFoundException("[sign contract] email, contractId : " + me.getEmail() + ", " + cancelContractId);

        // 계약 참여자들 모두 가져오고, 나의 Sign에 서명 후
        // Sign 모두 Y가 되면 블록체인으로 계약 체결, 계약서 상태 CANCELED로 바꿈.

        CancelSign myCancelSign = cancelSigns.stream()
                .filter(sign -> sign.getUser().getEmail().equals(me.getEmail()))
                .findFirst().orElseThrow(() -> new NotFoundException("[sign contract] email, contractId : " + me.getEmail() + ", " + cancelContractId));

        //이미 서명 했으면 409 응답
        if(myCancelSign.getSignState().equals(SignState.Y))
            throw new DuplicateSignException("contractId, email : " + cancelContractId + ", " + me.getEmail());

        myCancelSign.sign();

        boolean allY = cancelSigns.stream()
                .allMatch(sign -> sign.getSignState().equals(SignState.Y));

        if(allY) {
            CancelContract cancelContract = cancelContractRepository.findById(cancelContractId).orElseThrow(()->new NotFoundException("[sign cancel contract] email, contractId : " + me.getEmail() + ", " + cancelContractId));

            cancelContract.updateStateToCanceled();

            //TODO: 블록체인으로 계약 체결되도록

        }

    }
}
