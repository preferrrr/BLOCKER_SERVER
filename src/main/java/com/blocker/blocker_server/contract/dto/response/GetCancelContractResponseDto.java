package com.blocker.blocker_server.contract.dto.response;

import com.blocker.blocker_server.contract.domain.CancelContract;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetCancelContractResponseDto {
    private Long cancelContractId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    private GetCancelContractResponseDto(Long cancelContractId, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.cancelContractId = cancelContractId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static GetCancelContractResponseDto of(CancelContract cancelContract) {
        return GetCancelContractResponseDto.builder()
                .cancelContractId(cancelContract.getCancelContractId())
                .title(cancelContract.getTitle())
                .content(cancelContract.getContent())
                .createdAt(cancelContract.getCreatedAt())
                .modifiedAt(cancelContract.getModifiedAt())
                .build();
    }
}
