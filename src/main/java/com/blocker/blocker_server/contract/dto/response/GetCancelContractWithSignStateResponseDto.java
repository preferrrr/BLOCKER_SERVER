package com.blocker.blocker_server.contract.dto.response;

import com.blocker.blocker_server.contract.domain.CancelContract;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class GetCancelContractWithSignStateResponseDto {
    private Long cancelContractId;
    private Long contractId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CancelContractorAndSignState> contractorAndSignStates;

    @Builder
    private GetCancelContractWithSignStateResponseDto(Long cancelContractId, Long contractId, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, List<CancelContractorAndSignState> contractorAndSignStates) {
        this.cancelContractId = cancelContractId;
        this.contractId = contractId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.contractorAndSignStates = contractorAndSignStates;
    }

    public static GetCancelContractWithSignStateResponseDto of(CancelContract cancelContract, List<CancelContractorAndSignState> contractorAndSignStates) {
        return GetCancelContractWithSignStateResponseDto.builder()
                .cancelContractId(cancelContract.getCancelContractId())
                .contractId(cancelContract.getContract().getContractId())
                .title(cancelContract.getTitle())
                .content(cancelContract.getContent())
                .createdAt(cancelContract.getCreatedAt())
                .modifiedAt(cancelContract.getModifiedAt())
                .contractorAndSignStates(contractorAndSignStates)
                .build();
    }


}
