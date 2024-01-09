package com.blocker.blocker_server.contract.service;

import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.domain.ContractState;
import com.blocker.blocker_server.contract.dto.request.ModifyContractRequestDto;
import com.blocker.blocker_server.contract.dto.request.SaveContractRequestDto;
import com.blocker.blocker_server.contract.dto.response.ContractorAndSignState;
import com.blocker.blocker_server.contract.dto.response.GetConcludeContractResponseDto;
import com.blocker.blocker_server.contract.dto.response.GetContractResponseDto;
import com.blocker.blocker_server.contract.dto.response.GetProceedContractResponseDto;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ContractService {

    private final ContractServiceSupport contractServiceSupport;

    @Transactional
    public void saveContract(User user, SaveContractRequestDto requestDto) {

        Contract contract = Contract.create(user, requestDto.getTitle(), requestDto.getContent());

        contractServiceSupport.saveContract(contract);
    }

    @Transactional
    public void modifyContract(User user, Long contractId, ModifyContractRequestDto requestDto) {

        //수정할 계약서
        Contract contract = contractServiceSupport.getContractById(contractId);

        //체결 완료된 계약서는 수정하지 못함
        contractServiceSupport.checkIsConcludeContractForModify(user.getEmail(), contract);

        //계약 진행 중인 계약서를 수정하면 서명들 다시 N으로 바뀜
        contractServiceSupport.checkIsProceedContractForModify(contract);

        contract.modifyContract(requestDto.getTitle(), requestDto.getContent());

    }

    public List<GetContractResponseDto> getContracts(User user, ContractState state) {

        List<Contract> contracts = contractServiceSupport.getContractsByUserAndState(user.getEmail(), state);

        return contractServiceSupport.entityListToDtoList(contracts);

    }

    public GetContractResponseDto getNotProceedContract(Long contractId) {

        //조회할 계약서
        Contract contract = contractServiceSupport.getContractById(contractId);

        //NOT PROCEED가 맞는지 검사
        contractServiceSupport.checkIsNotProceedContract(contract);

        return GetContractResponseDto.of(contract);
    }

    @Transactional
    public void deleteContract(User user, Long contractId) {

        //삭제할 계약서
        Contract contract = contractServiceSupport.getContractById(contractId);

        //계약서의 작성자인지 검사
        contractServiceSupport.checkIsContractWriter(user.getEmail(), contract);

        //미체결 계약서인지 검사
        contractServiceSupport.checkIsNotProceedContract(contract);

        //계약서가 포함된 게시글이 있는지 검사
        contractServiceSupport.checkExistsBoardBelongingToContract(contract);

        //삭제
        contractServiceSupport.deleteContractById(contract.getContractId());

    }

    @Transactional
    public void deleteContractWithBoards(User me, Long contractId) {
        //삭제할 계약서
        Contract contract = contractServiceSupport.getContractById(contractId);

        //계약서의 작성자인지 검사
        contractServiceSupport.checkIsContractWriter(me.getEmail(), contract);

        //미체결 계약서인지 검사
        contractServiceSupport.checkIsNotProceedContract(contract);

        //cascade 옵션으로 게시글까지 모두 지움
        contractServiceSupport.deleteContractById(contractId);
    }


    public GetProceedContractResponseDto getProceedContract(User me, Long contractId) {

        //계약서 참가자들 서명과 함께 조회
        Contract contract = contractServiceSupport.getContractWIthSignsById(contractId);

        //진행 중 계약인지 검사
        contractServiceSupport.checkIsProceedContract(contract);

        //계약 참가자인지 검사
        contractServiceSupport.checkIsParticipant(me, contract);

        //참가자들 서명 상태 반환
        List<ContractorAndSignState> signs = contractServiceSupport.getContractorAndSignState(contract.getAgreementSigns());

        return  GetProceedContractResponseDto.of(contract, signs);
    }

    public GetConcludeContractResponseDto getConcludeContract(User me, Long contractId) {

        //계약서 참가자들 서명과 함께 조회
        Contract contract = contractServiceSupport.getContractWIthSignsById(contractId);

        //체결된 계약인지 검사
        contractServiceSupport.checkIsConcludeContract(contract);

        //계약 참가자인지 검사
        contractServiceSupport.checkIsParticipant(me, contract);

        List<ContractorAndSignState> signs = contractServiceSupport.getContractorAndSignState(contract.getAgreementSigns());

        return  GetConcludeContractResponseDto.of(contract, signs);
    }
}
