package com.blocker.blocker_server.service;

import com.blocker.blocker_server.dto.response.ContractorAndSignState;
import com.blocker.blocker_server.dto.response.GetCancelContractResponse;
import com.blocker.blocker_server.dto.response.GetContractResponseDto;
import com.blocker.blocker_server.entity.*;
import com.blocker.blocker_server.exception.ForbiddenException;
import com.blocker.blocker_server.exception.NotCanceledContractException;
import com.blocker.blocker_server.exception.NotCancelingContractException;
import com.blocker.blocker_server.exception.NotFoundException;
import com.blocker.blocker_server.repository.CancelContractRepository;
import com.blocker.blocker_server.repository.CancelSignRepository;
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

    private GetCancelContractResponse buildDto(CancelContract cancelContract) {
        List<ContractorAndSignState> contractorAndSignStates = new ArrayList<>();

        cancelContract.getCancelSigns().stream().forEach(sign -> contractorAndSignStates.add(ContractorAndSignState.builder()
                .contractor(sign.getUser().getEmail())
                .signState(sign.getSignState())
                .build()));

        GetCancelContractResponse response = GetCancelContractResponse.builder()
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

    public GetCancelContractResponse getCancelingContract(User me, Long cancelContractId) {
        CancelContract cancelContract = cancelContractRepository.findCancelContractWithSignsById(cancelContractId).orElseThrow(() -> new NotFoundException("[get canceling contract] cancelContractId : " + cancelContractId));

        //파기 진행 중 계약서가 아니면 예외 반환
        if (!cancelContract.getCancelContractState().equals(CancelContractState.CANCELING))
            throw new NotCancelingContractException("cancelContractId : " + cancelContractId);

        if (!cancelContract.getUser().getEmail().equals(me.getEmail()))
            throw new ForbiddenException("[get canceling contract] cancelContractId, email: " + cancelContractId + ", " + me.getEmail());

        GetCancelContractResponse response = buildDto(cancelContract);

        return response;

    }

    public GetCancelContractResponse getCanceledContract(User me, Long cancelContractId) {
        CancelContract cancelContract = cancelContractRepository.findCancelContractWithSignsById(cancelContractId).orElseThrow(() -> new NotFoundException("[get canceled contract] cancelContractId : " + cancelContractId));

        //파기 진행 중 계약서가 아니면 예외 반환
        if (!cancelContract.getCancelContractState().equals(CancelContractState.CANCELED))
            throw new NotCanceledContractException("cancelContractId : " + cancelContractId);

        if (!cancelContract.getUser().getEmail().equals(me.getEmail()))
            throw new ForbiddenException("[get canceled contract] cancelContractId, email: " + cancelContractId + ", " + me.getEmail());

        GetCancelContractResponse response = buildDto(cancelContract);

        return response;

    }
}
