package com.blocker.blocker_server.contract.dto.response;

import com.blocker.blocker_server.contract.domain.Contract;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetProceedContractResponseDto {

    private Long contractId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<ContractorAndSignState> contractorAndSignStates;

    @Builder
    private GetProceedContractResponseDto(Long contractId, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, List<ContractorAndSignState> contractorAndSignStates) {
        this.contractId = contractId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.contractorAndSignStates = contractorAndSignStates;
    }

    public static GetProceedContractResponseDto of(Contract contract, List<ContractorAndSignState> contractorAndSignStates) {
        return GetProceedContractResponseDto.builder()
                .contractId(contract.getContractId())
                .title(contract.getTitle())
                .content(contract.getContent())
                .createdAt(contract.getCreatedAt())
                .modifiedAt(contract.getModifiedAt())
                .contractorAndSignStates(contractorAndSignStates)
                .build();
    }
}
