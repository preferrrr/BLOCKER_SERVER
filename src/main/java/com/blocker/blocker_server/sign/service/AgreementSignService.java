package com.blocker.blocker_server.sign.service;

import com.blocker.blocker_server.chat.service.ChatService;
import com.blocker.blocker_server.commons.exception.*;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.domain.ContractState;
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

    private final AgreementSignRepository agreementSignRepository;
    private final UserRepository userRepository;
    private final ContractRepository contractRepository;
    private final ChatService chatService;

    @Transactional
    public void proceedContract(User me, ProceedSignRequestDto request) {

        Contract contract = contractRepository.findById(request.getContractId()).orElseThrow(() -> new NotFoundException("[proceed sign] contractId : " + request.getContractId()));

        //계약서를 쓴 사람이 내가 아니면 403 FORBIDDEN 반환
        if (!contract.getUser().getEmail().equals(me.getEmail()))
            throw new ForbiddenException("[proceed contract] contractId, email : " + contract.getContractId() + ", " + me.getEmail());

        //해당 계약서는 이미 계약을 진행 중이라면 409 conflict 반환
        if (agreementSignRepository.existsByContract(contract))
            throw new ExistsAgreementSignException("contractId : " + request.getContractId());

        contract.updateStateToProceed(); // 계약서의 상태를 진행 중으로 바꿈.

        List<AgreementSign> agreementSigns = request.getContractors().stream() // 계약에 참여하는 사람들
                .map(email -> userRepository.findByEmail(email)
                        .orElseThrow(() -> new NotFoundException("[proceed sign] email : " + email)))
                .map(user -> AgreementSign.create(user, contract))
                .collect(Collectors.toList());

        agreementSigns.add(AgreementSign.create(me, contract)); // 나도 계약 참여자. 나도 나중에 서명해야함.

        //서명 상태는 기본 N으로 되어 있음.
        // 1. 좀 더 확실하게 하려면 초대받은 사람들은, 초대를 승낙한다는 것이 있어야하지 않을까?
        //    그게 아니라면 어차피 서명을 안 하면 됨.
        // 2. 계약에 참여할 사람들을 직접 입력이 아닌, 검색 후 선택할건데 굳이 또 유저들을 조회해야할까 ?
        //    계약의 특성상 잘못되는 것이 있으면 안되기 때문에 쿼리가 나가더라도 조회하는 쪽으로.

        agreementSignRepository.saveAll(agreementSigns);

        //계약 참여자들끼리 단체 채팅방 만들어줌.
        chatService.createChatRoom(me, request.getContractors());
    }

    @Transactional
    public void signContract(User me, Long contractId) {

        List<AgreementSign> agreementSigns = agreementSignRepository.findByContract(contractRepository.getReferenceById(contractId));

        if (agreementSigns.size() < 2)
            throw new NotFoundException("[sign contract] email, contractId : " + me.getEmail() + ", " + contractId);

        sign(agreementSigns, me.getEmail(), contractId);

        isAllAgree(agreementSigns, me.getEmail(), contractId);

    }

    @Transactional
    public void breakContract(User me, Long contractId) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new NotFoundException("[break contract] : contractId : " + contractId));

        //진행 중 계약서가 아님.
        if (!contract.getContractState().equals(ContractState.PROCEED))
            throw new NotProceedContractException("email, contractId : " + me.getEmail() + ", " + contractId);

        List<AgreementSign> agreementSigns = agreementSignRepository.findByContract(contract);

        //계약 참여자가 맞는지 검사
        isContractor(agreementSigns, me.getEmail(), contractId);

        agreementSignRepository.deleteAll(agreementSigns);

        contract.updateStateToNotProceed();

    }

    private void isAllAgree(List<AgreementSign> agreementSigns, String myEmail, Long contractId) {
        boolean allY = agreementSigns.stream()
                .allMatch(sign -> sign.getSignState().equals(SignState.Y));

        if (allY) {
            Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new NotFoundException("[sign contract] email, contractId : " + myEmail + ", " + contractId));

            contract.updateStateToConclude();

            //TODO: 블록체인으로 계약 체결되도록

        }
    }

    @Transactional
    private void sign(List<AgreementSign> agreementSigns, String myEmail, Long contractId) {

        // 계약 참여자들 모두 가져오고, 나의 Sign에 서명 후
        // Sign 모두 Y가 되면 블록체인으로 계약 체결, 계약서 상태 CONCLUDE로 바꿈.

        AgreementSign myAgreementSign = agreementSigns.stream() // signs 중 내 sign 찾기
                .filter(sign -> sign.getUser().getEmail().equals(myEmail))
                .findFirst().orElseThrow(() -> new NotFoundException("[sign contract] email, contractId : " + myEmail + ", " + contractId));

        //이미 서명 했으면 409 응답
        if (myAgreementSign.getSignState().equals(SignState.Y))
            throw new DuplicateSignException("contractId, email : " + contractId + ", " + myEmail);

        myAgreementSign.sign();

    }

    private void isContractor(List<AgreementSign> agreementSigns, String myEmail, Long contractId) {
        //계약 참여자가 아님.
        boolean isContractor = agreementSigns.stream()
                .anyMatch(sign -> sign.getUser().getEmail().equals(myEmail));
        if (!isContractor)
            throw new ForbiddenException("[break contract] email, contractId : " + myEmail + ", " + contractId);

    }
}
