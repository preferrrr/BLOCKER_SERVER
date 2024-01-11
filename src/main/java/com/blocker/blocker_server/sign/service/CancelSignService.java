package com.blocker.blocker_server.sign.service;

import com.blocker.blocker_server.contract.domain.CancelContract;
import com.blocker.blocker_server.contract.service.CancelContractServiceSupport;
import com.blocker.blocker_server.contract.service.ContractServiceSupport;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.sign.domain.CancelSign;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CancelSignService {

    private final CancelSignServiceSupport cancelSignServiceSupport;
    private final ContractServiceSupport contractServiceSupport;
    private final CancelContractServiceSupport cancelContractServiceSupport;

    @Transactional
    public void cancelContract(User me, Long contractId) {

        //파기를 진행할 계약서
        Contract contract = contractServiceSupport.getContractWIthSignsById(contractId);

        //계약서의 상태가 체결인지 검사
        cancelSignServiceSupport.checkIsConcludeContract(contract);

        //계약 참여자인지 검사
        cancelSignServiceSupport.checkIsParticipantForCancel(me, contract);

        //이미 파기 진행 중인지 검사
        cancelSignServiceSupport.checkIsCancelingContract(contract);

        //파기 계약서
        CancelContract cancelContract = CancelContract.create(me, contract, contract.getTitle(), contract.getContent());

        //파기 서명들
        List<CancelSign> cancelSigns = cancelSignServiceSupport.createCancelSigns(cancelContract, contract.getAgreementSigns());

        //파기 계약서와 파기 서명 저장
        cancelSignServiceSupport.saveCancelContract(cancelContract);
        cancelSignServiceSupport.saveCancelSigns(cancelSigns);

    }

    @Transactional
    public void signCancelContract(User me, Long cancelContractId) {

        //계약서 서명과 함께 조회
        CancelContract cancelContract = cancelContractServiceSupport.getCancelContractWithSignsById(cancelContractId);

        //내 서명
        CancelSign myCancelSign = cancelSignServiceSupport.getMyCancelSign(me.getEmail(), cancelContract.getCancelSigns());

        //이미 서명 했는지 검사
        cancelSignServiceSupport.checkMySignStateIsN(myCancelSign);

        //서명
        myCancelSign.sign();

        //계약 참여자들이 모두 서명했는지 검사
        //모두 서명했다면 계약서는 체결로 바뀌고, 블록체인에 저장됨
        cancelSignServiceSupport.checkIsAllAgree(cancelContract);
    }

}
