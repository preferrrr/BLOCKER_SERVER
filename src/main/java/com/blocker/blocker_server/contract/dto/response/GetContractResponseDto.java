package com.blocker.blocker_server.contract.dto.response;

import com.blocker.blocker_server.contract.domain.Contract;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetContractResponseDto {
    private Long contractId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    private GetContractResponseDto(Long contractId, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.contractId = contractId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static GetContractResponseDto of(Contract contract) {
        return GetContractResponseDto.builder()
                .contractId(contract.getContractId())
                .title(contract.getTitle())
                .content(contract.getContent())
                .createdAt(contract.getCreatedAt())
                .modifiedAt(contract.getModifiedAt())
                .build();
    }
}
