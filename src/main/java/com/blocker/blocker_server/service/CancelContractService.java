package com.blocker.blocker_server.service;

import com.blocker.blocker_server.dto.response.GetContractResponseDto;
import com.blocker.blocker_server.entity.CancelContract;
import com.blocker.blocker_server.entity.CancelContractState;
import com.blocker.blocker_server.entity.User;
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
    private final CancelSignRepository cancelSignRepository;

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
}
