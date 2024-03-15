package com.blocker.blocker_server.contract.service;

import com.blocker.blocker_server.contract.domain.CancelContract;
import com.blocker.blocker_server.contract.domain.CancelContractState;
import com.blocker.blocker_server.contract.dto.response.CancelContractorAndSignState;
import com.blocker.blocker_server.contract.dto.response.GetCancelContractResponseDto;
import com.blocker.blocker_server.contract.exception.CancelContractNotFoundException;
import com.blocker.blocker_server.contract.exception.IsNotCancelContractParticipant;
import com.blocker.blocker_server.contract.exception.IsNotCanceledCancelContract;
import com.blocker.blocker_server.contract.exception.IsNotCancelingCancelContract;
import com.blocker.blocker_server.contract.repository.CancelContractRepository;
import com.blocker.blocker_server.sign.domain.CancelSign;
import com.blocker.blocker_server.sign.repository.CancelSignRepository;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CancelContractServiceSupport {

    private final CancelContractRepository cancelContractRepository;
    private final CancelSignRepository cancelSignRepository;

    public List<CancelContract> getCancelContractsByUserAndState(User me, CancelContractState state) {
        return cancelContractRepository.findCancelContractsByUserAndState(me, state);
    }

    public List<GetCancelContractResponseDto> entityListToDtoList(List<CancelContract> cancelContracts) {
        return cancelContracts.stream()
                .map(cancelContract -> GetCancelContractResponseDto.of(cancelContract))
                .collect(Collectors.toList());
    }

    public List<CancelContractorAndSignState> getCancelContractorAndSignState(List<CancelSign> cancelSigns) {
        return cancelSigns.stream()
                .map(sign -> CancelContractorAndSignState.of(sign.getUser().getName(), sign.getSignState()))
                .collect(Collectors.toList());
    }

    public CancelContract getCancelContractWithSignsById(Long cancelContractId) {
        return cancelContractRepository.findCancelContractWithSignsById(cancelContractId).orElseThrow(CancelContractNotFoundException::new);
    }

    public void checkIsCancelingCancelContract(CancelContract cancelContract) {
        if (!cancelContract.getCancelContractState().equals(CancelContractState.CANCELING))
            throw new IsNotCancelingCancelContract();
    }

    public void checkIsCancelContractParticipant(User user, CancelContract cancelContract) {
        if (!cancelSignRepository.existsByUserAndCancelContract(user, cancelContract))
            throw new IsNotCancelContractParticipant();
    }

    public void checkIsCanceledCancelContract(CancelContract cancelContract) {
        if (!cancelContract.getCancelContractState().equals(CancelContractState.CANCELED))
            throw new IsNotCanceledCancelContract();
    }
}
