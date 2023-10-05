package com.blocker.blocker_server.service;

import com.blocker.blocker_server.dto.request.SaveModifyContractRequestDto;
import com.blocker.blocker_server.dto.response.GetContractResponseDto;
import com.blocker.blocker_server.entity.*;
import com.blocker.blocker_server.exception.*;
import com.blocker.blocker_server.repository.BoardRepository;
import com.blocker.blocker_server.repository.ContractRepository;
import com.blocker.blocker_server.repository.SignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ContractService {

    private final ContractRepository contractRepository;
    private final SignRepository signRepository;
    private final BoardRepository boardRepository;

    public void saveContract(User user, SaveModifyContractRequestDto requestDto) {
        Contract contract = Contract.builder()
                .user(user)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();

        contractRepository.save(contract);
    }

    public void modifyContract(User user, Long contractId, SaveModifyContractRequestDto requestDto) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new NotFoundException("[modify contract] contractId : " + contractId));

        if (!contract.getUser().getEmail().equals(user.getEmail())) // 내가 적은 계약서가 아님.
            throw new ForbiddenException("[modify contract] contractId, email : " + contractId + ", " + user.getEmail());

        //계약서 미체결, 진행 중일 때는 수정 가능. 계약이 체결됐을 때는 수정할 수 없다.
        //미체결일 경우 그냥 수정만 하면 돼.
        //진행 중일 경우에는 참여자들의 서명을 모두 취소해야함.
        if (contract.getContractState().equals(ContractState.CONCLUDE)) // 체결완료된 계약서
            throw new NotAllowModifyContractException("contractId : " + contractId);
        else if (contract.getContractState().equals(ContractState.PROCEED)) { // 진행 중 계약서
            List<Sign> signList = signRepository.findByContract(contract);
            signList.stream()
                    .filter(sign -> sign.getSignState().equals(SignState.Y))
                    .forEach(sign -> sign.cancel());
        }
        contract.modifyContract(requestDto.getTitle(), requestDto.getContent());

    }

    public List<GetContractResponseDto> getContracts(User user, ContractState state) {

        List<Contract> contracts;

        if (state.equals(ContractState.NOT_PROCEED))
            contracts = contractRepository.findNotProceedContracts(user, state);
        else
            contracts = getProceedOrConcludeContract(user, state);

        List<GetContractResponseDto> dtos = new ArrayList<>();

        for (Contract contract : contracts) {
            GetContractResponseDto dto = GetContractResponseDto.builder()
                    .contractId(contract.getContractId())
                    .title(contract.getTitle())
                    .content(contract.getContent())
                    .createdAt(contract.getCreatedAt())
                    .modifiedAt(contract.getModifiedAt())
                    .build();

            dtos.add(dto);
        }

        return dtos;

    }

    private List<Contract> getProceedOrConcludeContract(User user, ContractState state) {

        List<Sign> signs = signRepository.findByUser(user);

        List<Long> contractIds = new ArrayList<>();

        signs.stream().forEach(sign -> contractIds.add(sign.getContract().getContractId()));

        List<Contract> contracts = contractRepository.findByContractIdInAndContractState(contractIds, state);

        return contracts;
    }

    public GetContractResponseDto getContract(Long contractId) {

        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new NotFoundException("[get contract] contractId : " + contractId));

        if(!contract.getContractState().equals(ContractState.NOT_PROCEED))
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
}
