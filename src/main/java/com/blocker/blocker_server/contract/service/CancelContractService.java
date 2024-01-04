package com.blocker.blocker_server.contract.service;

import com.blocker.blocker_server.contract.domain.CancelContract;
import com.blocker.blocker_server.contract.domain.CancelContractState;
import com.blocker.blocker_server.contract.dto.response.ContractorAndSignState;
import com.blocker.blocker_server.contract.dto.response.GetCancelContractResponseDto;
import com.blocker.blocker_server.contract.dto.response.GetCancelContractWithSignStateResponseDto;
import com.blocker.blocker_server.commons.exception.ForbiddenException;
import com.blocker.blocker_server.commons.exception.NotCanceledContractException;
import com.blocker.blocker_server.commons.exception.NotCancelingContractException;
import com.blocker.blocker_server.commons.exception.NotFoundException;
import com.blocker.blocker_server.contract.repository.CancelContractRepository;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CancelContractService {

    private final CancelContractRepository cancelContractRepository;

    public List<GetCancelContractResponseDto> getCancelContractList(User me, CancelContractState state) {

        List<CancelContract> cancelContracts = cancelContractRepository.findByUserAndCancelContractState(me, state);

        List<GetCancelContractResponseDto> response = cancelContracts.stream()
                .map(cancelContract -> GetCancelContractResponseDto.of(cancelContract))
                .collect(Collectors.toList());

        return response;
    }

    private GetCancelContractWithSignStateResponseDto buildDto(CancelContract cancelContract) {

        List<ContractorAndSignState> contractorAndSignStates = cancelContract.getCancelSigns().stream()
                .map(sign -> ContractorAndSignState.of(sign.getUser().getEmail(), sign.getSignState()))
                .collect(Collectors.toList());

        GetCancelContractWithSignStateResponseDto response = GetCancelContractWithSignStateResponseDto.of(cancelContract, contractorAndSignStates);

        return response;
    }

    public GetCancelContractWithSignStateResponseDto getCancelingContract(User me, Long cancelContractId) {
        CancelContract cancelContract = cancelContractRepository.findCancelContractWithSignsById(cancelContractId).orElseThrow(() -> new NotFoundException("[get canceling contract] cancelContractId : " + cancelContractId));

        //파기 진행 중 계약서가 아니면 예외 반환
        if (!cancelContract.getCancelContractState().equals(CancelContractState.CANCELING))
            throw new NotCancelingContractException("cancelContractId : " + cancelContractId);

        if (!cancelContract.getUser().getEmail().equals(me.getEmail()))
            throw new ForbiddenException("[get canceling contract] cancelContractId, email: " + cancelContractId + ", " + me.getEmail());

        return buildDto(cancelContract);

    }

    public GetCancelContractWithSignStateResponseDto getCanceledContract(User me, Long cancelContractId) {

        CancelContract cancelContract = cancelContractRepository.findCancelContractWithSignsById(cancelContractId).orElseThrow(() -> new NotFoundException("[get canceled contract] cancelContractId : " + cancelContractId));

        //파기 진행 중 계약서가 아니면 예외 반환
        if (!cancelContract.getCancelContractState().equals(CancelContractState.CANCELED))
            throw new NotCanceledContractException("cancelContractId : " + cancelContractId);

        if (!cancelContract.getUser().getEmail().equals(me.getEmail()))
            throw new ForbiddenException("[get canceled contract] cancelContractId, email: " + cancelContractId + ", " + me.getEmail());

        return buildDto(cancelContract);

    }
}
