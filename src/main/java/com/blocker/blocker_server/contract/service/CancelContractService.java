package com.blocker.blocker_server.contract.service;

import com.blocker.blocker_server.contract.domain.CancelContract;
import com.blocker.blocker_server.contract.domain.CancelContractState;
import com.blocker.blocker_server.contract.dto.response.ContractorAndSignState;
import com.blocker.blocker_server.contract.dto.response.GetCancelContractResponseDto;
import com.blocker.blocker_server.contract.dto.response.GetContractResponseDto;
import com.blocker.blocker_server.commons.exception.ForbiddenException;
import com.blocker.blocker_server.commons.exception.NotCanceledContractException;
import com.blocker.blocker_server.commons.exception.NotCancelingContractException;
import com.blocker.blocker_server.commons.exception.NotFoundException;
import com.blocker.blocker_server.contract.repository.CancelContractRepository;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CancelContractService {

    private final CancelContractRepository cancelContractRepository;

    public List<GetContractResponseDto> getCancelContractList(User me, CancelContractState state) {
        List<CancelContract> cancelContracts = cancelContractRepository.findByUserAndCancelContractState(me, state);

        List<GetContractResponseDto> dtos = new ArrayList<>();
        cancelContracts.stream().forEach(cancelContract -> dtos.add(GetContractResponseDto.builder()
                .contractId(cancelContract.getCancelContractId())
                .title(cancelContract.getTitle())
                .content(cancelContract.getContent())
                .createdAt(cancelContract.getCreatedAt())
                .modifiedAt(cancelContract.getModifiedAt())
                .build()));

        return dtos;
    }

    private GetCancelContractResponseDto buildDto(CancelContract cancelContract) {
        List<ContractorAndSignState> contractorAndSignStates = new ArrayList<>();

        cancelContract.getCancelSigns().stream().forEach(sign -> contractorAndSignStates.add(ContractorAndSignState.builder()
                .contractor(sign.getUser().getEmail())
                .signState(sign.getSignState())
                .build()));

        GetCancelContractResponseDto response = GetCancelContractResponseDto.builder()
                .cancelContractId(cancelContract.getCancelContractId())
                .contractId(cancelContract.getContract().getContractId())
                .title(cancelContract.getTitle())
                .content(cancelContract.getContent())
                .createdAt(cancelContract.getCreatedAt())
                .modifiedAt(cancelContract.getModifiedAt())
                .contractorAndSignStates(contractorAndSignStates)
                .build();

        return response;
    }

    public GetCancelContractResponseDto getCancelingContract(User me, Long cancelContractId) {
        CancelContract cancelContract = cancelContractRepository.findCancelContractWithSignsById(cancelContractId).orElseThrow(() -> new NotFoundException("[get canceling contract] cancelContractId : " + cancelContractId));

        //파기 진행 중 계약서가 아니면 예외 반환
        if (!cancelContract.getCancelContractState().equals(CancelContractState.CANCELING))
            throw new NotCancelingContractException("cancelContractId : " + cancelContractId);

        if (!cancelContract.getUser().getEmail().equals(me.getEmail()))
            throw new ForbiddenException("[get canceling contract] cancelContractId, email: " + cancelContractId + ", " + me.getEmail());

        GetCancelContractResponseDto response = buildDto(cancelContract);

        return response;

    }

    public GetCancelContractResponseDto getCanceledContract(User me, Long cancelContractId) {
        CancelContract cancelContract = cancelContractRepository.findCancelContractWithSignsById(cancelContractId).orElseThrow(() -> new NotFoundException("[get canceled contract] cancelContractId : " + cancelContractId));

        //파기 진행 중 계약서가 아니면 예외 반환
        if (!cancelContract.getCancelContractState().equals(CancelContractState.CANCELED))
            throw new NotCanceledContractException("cancelContractId : " + cancelContractId);

        if (!cancelContract.getUser().getEmail().equals(me.getEmail()))
            throw new ForbiddenException("[get canceled contract] cancelContractId, email: " + cancelContractId + ", " + me.getEmail());

        GetCancelContractResponseDto response = buildDto(cancelContract);

        return response;

    }
}