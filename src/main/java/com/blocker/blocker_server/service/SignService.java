package com.blocker.blocker_server.service;

import com.blocker.blocker_server.dto.request.ProceedSignRequest;
import com.blocker.blocker_server.entity.*;
import com.blocker.blocker_server.exception.*;
import com.blocker.blocker_server.repository.ContractRepository;
import com.blocker.blocker_server.repository.SignRepository;
import com.blocker.blocker_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SignService {

    private final SignRepository signRepository;
    private final UserRepository userRepository;
    private final ContractRepository contractRepository;

    public void proceedContract(User me, ProceedSignRequest request) {

        Contract contract = contractRepository.findById(request.getContractId()).orElseThrow(() -> new NotFoundException("[proceed sign] contractId : " + request.getContractId()));

        //계약서를 쓴 사람이 내가 아니면 403 FORBIDDEN 반환
        if (!contract.getUser().getEmail().equals(me.getEmail()))
            throw new ForbiddenException("[proceed contract] contractId, email : " + contract.getContractId() + ", " + me.getEmail());

        //해당 계약서는 이미 계약을 진행 중이라면 409 conflict 반환
        if (signRepository.existsByContract(contract))
            throw new ExistsProceededContractException("contractId : " + request.getContractId());

        contract.updateStateToProceed(); // 계약서의 상태를 진행 중으로 바꿈.

        List<String> emails = request.getContractors(); // 계약에 참여할 사람들의 이메일 리스트

        List<Sign> signs = new ArrayList<>(); // DB에 저장될 Sign 엔티티

        signs.add(Sign.builder().user(me).contract(contract).build()); // 나도 계약 참여자. 나도 나중에 서명해야함.

        for (String email : emails) {
            signs.add(Sign.builder()
                    .user(userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("[proceed sign] email : " + email)))
                    .contract(contract)
                    .build());
        }
        //서명 상태는 기본 N으로 되어 있음.
        // 1. 좀 더 확실하게 하려면 초대받은 사람들은, 초대를 승낙한다는 것이 있어야하지 않을까?
        //    그게 아니라면 어차피 서명을 안 하면 됨.
        // 2. 계약에 참여할 사람들을 직접 입력이 아닌, 검색 후 선택할건데 굳이 또 유저들을 조회해야할까 ?
        //    계약의 특성상 잘못되는 것이 있으면 안되기 때문에 쿼리가 나가더라도 조회하는 쪽으로.

        signRepository.saveAll(signs);

    }

    public void signContract(User me, Long contractId) {

//        Sign mySign = signRepository.findByContractAndUser(contractRepository.getReferenceById(contractId), me)
//                .orElseThrow(() -> new NotFoundException("[sign contract] email, contractId : " + me.getEmail() + ", " + contractId));
//
//        if(mySign.getSignState().equals(SignState.Y))
//            throw new DuplicateSignException("contractId, email : " + contractId + ", " + me.getEmail());

        List<Sign> signs = signRepository.findByContract(contractRepository.getReferenceById(contractId));

        if (signs.size() < 2)
            throw new NotFoundException("[sign contract] email, contractId : " + me.getEmail() + ", " + contractId);

        // 계약 참여자들 모두 가져오고, 나의 Sign에 서명 후
        // Sign 모두 Y가 되면 블록체인으로 계약 체결, 계약서 상태 CONCLUDE로 바꿈.

        Sign mySign = signs.stream()
                .filter(sign -> sign.getUser().getEmail().equals(me.getEmail()))
                .findFirst().orElseThrow(() -> new NotFoundException("[sign contract] email, contractId : " + me.getEmail() + ", " + contractId));

        //이미 서명 했으면 409 응답
        if(mySign.getSignState().equals(SignState.Y))
            throw new DuplicateSignException("contractId, email : " + contractId + ", " + me.getEmail());

        mySign.sign();

        boolean allY = signs.stream()
                .allMatch(sign -> sign.getSignState().equals(SignState.Y));

        if(allY) {
            Contract contract = contractRepository.findById(contractId).orElseThrow(()->new NotFoundException("[sign contract] email, contractId : " + me.getEmail() + ", " + contractId));

            contract.updateStateToConclude();

            //TODO: 블록체인으로 계약 체결되도록

        }

    }

    public void breakContract(User me, Long contractId) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new NotFoundException("[break contract] : contractId : " + contractId));

        //진행 중 계약서가 아님.
        if(!contract.getContractState().equals(ContractState.PROCEED))
            throw new NotProceedContractException("email, contractId : " + me.getEmail() + ", " + contractId);

        List<Sign> signs = signRepository.findByContract(contract);

        //계약 참여자가 아님.
        boolean isContractor = signs.stream()
                .anyMatch(sign -> sign.getUser().getEmail().equals(me.getEmail()));
        if (!isContractor)
            throw new ForbiddenException("[break contract] email, contractId : " + me.getEmail() + ", " + contractId);

        contract.updateStateToNotProceed();

        signRepository.deleteAll(signs);

    }
}
