package com.blocker.blocker_server.service;

import com.blocker.blocker_server.entity.*;
import com.blocker.blocker_server.exception.ExistsCancelSignException;
import com.blocker.blocker_server.exception.ForbiddenException;
import com.blocker.blocker_server.exception.NotConcludeContractException;
import com.blocker.blocker_server.exception.NotFoundException;
import com.blocker.blocker_server.repository.AgreementSignRepository;
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
        if(cancelSignRepository.existsByContract(contract))
            throw new ExistsCancelSignException("email, contractId : " + me.getEmail() + ", " + contractId);

        List<CancelSign> cancelSigns = new ArrayList<>();
        agreementSigns.stream().forEach(
                agreementSign -> cancelSigns.add(CancelSign.builder()
                        .user(agreementSign.getUser())
                        .contract(contract)
                        .build()));

        cancelSignRepository.saveAll(cancelSigns);
    }
}
