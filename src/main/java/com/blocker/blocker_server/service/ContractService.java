package com.blocker.blocker_server.service;

import com.blocker.blocker_server.dto.request.SaveModifyContractRequestDto;
import com.blocker.blocker_server.dto.response.GetContractResponseDto;
import com.blocker.blocker_server.entity.Contract;
import com.blocker.blocker_server.entity.ContractState;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.exception.ForbiddenException;
import com.blocker.blocker_server.exception.ModifyContractException;
import com.blocker.blocker_server.exception.NotFoundException;
import com.blocker.blocker_server.repository.ContractRepository;
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
        else if (contract.getContractState().equals(ContractState.PROCEED) ||
                contract.getContractState().equals(ContractState.CONCLUDE)) // 계약이 진행 중이거나 완료됐으면 수정하지 못함. 권한 없는 요청과 구분하기 위해 400으로.
            throw new ModifyContractException("[modify contract] contractId, email : " + contractId + ", " + user.getEmail());

        contract.modifyContract(requestDto.getTitle(), requestDto.getContent());

    }

    public List<GetContractResponseDto> getContracts(User user, ContractState state) {
        List<Contract> contracts = contractRepository.findNotConcludedContracts(user, state);

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

    public GetContractResponseDto getContract(Long contractId) {

        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new NotFoundException("[get contract] contractId : " + contractId));

        GetContractResponseDto dto = GetContractResponseDto.builder()
                .contractId(contractId)
                .title(contract.getTitle())
                .content(contract.getContent())
                .createdAt(contract.getCreatedAt())
                .modifiedAt(contract.getModifiedAt())
                .build();

        return dto;
    } //TODO : 계약서 수정은 진행 중일 때도 가능. but 진행 중이라면 계약 참여자들의 서명이 모두 취소되어야함.

    public void deleteContract(User user, Long contractId) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new NotFoundException("[delete contract] contractId : " + contractId));

        //TODO : 계약서 삭제는 진행 중일 때 가능.
        if(!user.getEmail().equals(contract.getUser().getEmail()))
            throw new ForbiddenException("[delete contract] contractId, email : " + contractId + ", " + user.getEmail());

        contractRepository.deleteById(contractId);
    }
}
