package com.blocker.blocker_server.sign.service;

import com.blocker.blocker_server.chat.service.ChatService;
import com.blocker.blocker_server.commons.exception.*;
import com.blocker.blocker_server.commons.utils.CurrentUserGetter;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.domain.ContractState;
import com.blocker.blocker_server.contract.service.ContractServiceSupport;
import com.blocker.blocker_server.sign.dto.request.ProceedSignRequestDto;
import com.blocker.blocker_server.contract.repository.ContractRepository;
import com.blocker.blocker_server.sign.repository.AgreementSignRepository;
import com.blocker.blocker_server.user.repository.UserRepository;
import com.blocker.blocker_server.sign.domain.AgreementSign;
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
public class AgreementSignService {

    private final AgreementSignServiceSupport agreementSignServiceSupport;
    private final ContractServiceSupport contractServiceSupport;
    private final ChatService chatService;
    private final CurrentUserGetter currentUserGetter;

    @Transactional
    public void proceedContract(ProceedSignRequestDto request) {

        User me = currentUserGetter.getCurrentUser();

        Contract contract = contractServiceSupport.getContractById(request.getContractId());

        //계약서 작성자인지 검사
        contractServiceSupport.checkIsContractWriter(me.getEmail(), contract);

        //미체결 계약서인지 검사
        agreementSignServiceSupport.checkIsNotProceedContract(contract);

        //혼자서는 계약 진행할 수 없음 => 참여자 리스트가 비어있는지 검사.
        agreementSignServiceSupport.checkIsEmptyContractor(request.getContractors());

        // 계약서의 상태를 진행 중으로 바꿈.
        contract.updateStateToProceed();

        //저장할 AgreementSign 리스트
        List<AgreementSign> agreementSigns = agreementSignServiceSupport.createAgreementSigns(contract, me, request.getContractors());

        //서명 상태는 기본 N으로 되어 있음.
        // 1. 좀 더 확실하게 하려면 초대받은 사람들은, 초대를 승낙한다는 것이 있어야하지 않을까?
        //    그게 아니라면 어차피 서명을 안 하면 됨.
        // 2. 계약에 참여할 사람들을 직접 입력이 아닌, 검색 후 선택할건데 굳이 또 유저들을 조회해야할까 ?
        //    계약의 특성상 잘못되는 것이 있으면 안되기 때문에 쿼리가 나가더라도 조회하는 쪽으로.

        //AgreementSign 리스트 저장
        agreementSignServiceSupport.saveAgreementSigns(agreementSigns);

        //계약 참여자들끼리 단체 채팅방 만들어줌.
        chatService.createChatRoom(request.getContractors());
    }

    @Transactional
    public void signContract(Long contractId) {

        User me = currentUserGetter.getCurrentUser();

        //계약서 서명과 함께 조회
        Contract contract = contractServiceSupport.getContractWIthSignsById(contractId);

        //내 서명
        AgreementSign mySign = agreementSignServiceSupport.getMyAgreementSign(me.getEmail(), contract.getAgreementSigns());

        //이미 서명 했는지 검사
        agreementSignServiceSupport.checkMySignStateIsN(mySign);

        //서명
        mySign.sign();

        //계약 참여자들이 모두 서명했는지 검사
        //모두 서명했다면 계약서는 체결로 바뀌고, 블록체인에 저장됨
        agreementSignServiceSupport.checkIsAllAgree(contract);

    }

    @Transactional
    public void breakContract(Long contractId) {

        User me = currentUserGetter.getCurrentUser();

        //진행 취소할 계약서
        Contract contract = contractServiceSupport.getContractWIthSignsById(contractId);

        //계약 참여자가 맞는지 검사
        contractServiceSupport.checkIsParticipant(me, contract);

        //진행 중 계약서가 맞는지 검사
        contractServiceSupport.checkIsProceedContract(contract);

        //서명들 삭제
        agreementSignServiceSupport.deleteAgreementSigns(contract.getAgreementSigns());

        //계약서 상태 NOT_PROCEED로 변경
        contract.updateStateToNotProceed();

    }


}
