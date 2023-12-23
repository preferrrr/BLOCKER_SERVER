package com.blocker.blocker_server.contract.service;

import com.blocker.blocker_server.commons.exception.*;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.domain.ContractState;
import com.blocker.blocker_server.contract.dto.request.SaveOrModifyContractRequestDto;
import com.blocker.blocker_server.contract.dto.response.ContractorAndSignState;
import com.blocker.blocker_server.contract.dto.response.GetContractResponseDto;
import com.blocker.blocker_server.contract.dto.response.GetProceedOrConcludeContractResponseDto;
import com.blocker.blocker_server.board.repository.BoardRepository;
import com.blocker.blocker_server.contract.repository.ContractRepository;
import com.blocker.blocker_server.sign.repository.AgreementSignRepository;
import com.blocker.blocker_server.sign.domain.AgreementSign;
import com.blocker.blocker_server.sign.domain.SignState;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ContractService {

    private final ContractRepository contractRepository;
    private final AgreementSignRepository agreementSignRepository;
    private final BoardRepository boardRepository;

    public void saveContract(User user, SaveOrModifyContractRequestDto requestDto) {
        Contract contract = Contract.builder()
                .user(user)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();

        contractRepository.save(contract);
    }

    public void modifyContract(User user, Long contractId, SaveOrModifyContractRequestDto requestDto) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new NotFoundException("[modify contract] contractId : " + contractId));

        if (!contract.getUser().getEmail().equals(user.getEmail())) // 내가 적은 계약서가 아님.
            throw new ForbiddenException("[modify contract] contractId, email : " + contractId + ", " + user.getEmail());

        //계약서 미체결, 진행 중일 때는 수정 가능. 계약이 체결됐을 때는 수정할 수 없다.
        //미체결일 경우 그냥 수정만 하면 돼.
        //진행 중일 경우에는 참여자들의 서명을 모두 취소해야함.
        if (contract.getContractState().equals(ContractState.CONCLUDE)) // 체결완료된 계약서
            throw new NotAllowModifyContractException("contractId : " + contractId);
        else if (contract.getContractState().equals(ContractState.PROCEED)) { // 진행 중 계약서
            List<AgreementSign> agreementSignList = agreementSignRepository.findByContract(contract);
            agreementSignList.stream()
                    .filter(sign -> sign.getSignState().equals(SignState.Y))
                    .forEach(sign -> sign.cancel());
        }
        contract.modifyContract(requestDto.getTitle(), requestDto.getContent());

    }

    public List<GetContractResponseDto> getContracts(User user, ContractState state) {

        List<Contract> contracts = getContractEntities(user, state);

        List<GetContractResponseDto> response = contracts.stream()
                .map(contract -> GetContractResponseDto.builder()
                        .contractId(contract.getContractId())
                        .title(contract.getTitle())
                        .content(contract.getContent())
                        .createdAt(contract.getCreatedAt())
                        .modifiedAt(contract.getModifiedAt())
                        .build())
                .collect(Collectors.toList());

        return response;

    }

    public GetContractResponseDto getContract(Long contractId) {

        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new NotFoundException("[get contract] contractId : " + contractId));

        if (!contract.getContractState().equals(ContractState.NOT_PROCEED))
            throw new IsNotProceedContractException("contractId : " + contractId);

        GetContractResponseDto dto = GetContractResponseDto.builder()
                .contractId(contractId)
                .title(contract.getTitle())
                .content(contract.getContent())
                .createdAt(contract.getCreatedAt())
                .modifiedAt(contract.getModifiedAt())
                .build();

        return dto;
    }

    public void deleteContract(User user, Long contractId) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new NotFoundException("[delete contract] contractId : " + contractId));

        //미체결 계약서 삭제이기 때문에
        //계약서의 작성자이어야 하고, 진행 중 다음이라면 삭제하지 못함.
        if (!user.getEmail().equals(contract.getUser().getEmail()) || !contract.getContractState().equals(ContractState.NOT_PROCEED))
            throw new ForbiddenException("[delete contract] contractId, email : " + contractId + ", " + user.getEmail());

        if (boardRepository.existsByContract(contract))
            throw new NotAllowDeleteContractException("[delete contract] contractId : " + contractId);

        contractRepository.deleteById(contractId);
    }

    public GetProceedOrConcludeContractResponseDto getProceedContract(User me, Long contractId) {

        GetProceedOrConcludeContractResponseDto dto = getProceedOrConcludeContractList(me, contractId, ContractState.PROCEED);

        return dto;
    }

    public GetProceedOrConcludeContractResponseDto getConcludeContract(User me, Long contractId) {

        GetProceedOrConcludeContractResponseDto dto = getProceedOrConcludeContractList(me, contractId, ContractState.CONCLUDE);

        return dto;
    }

    public GetProceedOrConcludeContractResponseDto getProceedOrConcludeContractList(User me, Long contractId, ContractState state) {

        Contract contract = contractRepository.findProceedContractWithSignById(contractId).orElseThrow(() -> new NotFoundException("contractId : " + contractId));

        if (!contract.getContractState().equals(state))
            throw new IsProceedContractException("contractId : " + contractId);

        List<AgreementSign> agreementSigns = contract.getAgreementSigns();

        //계약 참여자가 아니면 진행 중 계약서는 볼 수 없음.
        if (agreementSigns.stream().noneMatch(sign -> sign.getUser().getEmail().equals(me.getEmail()))) {
            throw new ForbiddenException("[get proceed contract] contractId, email" + contractId + ", " + me.getEmail());
        }

        List<ContractorAndSignState> contractorAndSignStates = new ArrayList<>();
        agreementSigns.stream().forEach(sign -> contractorAndSignStates.add(
                ContractorAndSignState.builder()
                        .contractor(sign.getUser().getName())
                        .signState(sign.getSignState())
                        .build()));

        GetProceedOrConcludeContractResponseDto dto = GetProceedOrConcludeContractResponseDto.builder()
                .contractId(contractId)
                .title(contract.getTitle())
                .content(contract.getContent())
                .createdAt(contract.getCreatedAt())
                .modifiedAt(contract.getModifiedAt())
                .contractorAndSignStates(contractorAndSignStates)
                .build();

        return dto;

    }

    public void deleteContractWithBoards(User me, Long contractId) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new NotFoundException("[delete contract with boards] contractId : " + contractId));

        if (!contract.getUser().getEmail().equals(me.getEmail()))
            throw new ForbiddenException("[delete contract with boards] contractId, email : " + contractId + ", " + me.getEmail());

        if (!contract.getContractState().equals(ContractState.NOT_PROCEED))
            throw new IsNotProceedContractException("contractId : " + contractId);

        contractRepository.deleteById(contractId); // OntToMany의 cascade 옵션에 의해 자식들도 모두 지워짐.

    }

    private List<Contract> getContractEntities(User user, ContractState state) {
        if (state.equals(ContractState.NOT_PROCEED))
            return contractRepository.findNotProceedContracts(user, state);
        else
            return contractRepository.findProceedOrConcludeContractList(user, state);
    }
}
