package com.blocker.blocker_server.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class GetCancelContractResponse {
    private Long cancelContractId;
    private Long contractId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<ContractorAndSignState> contractorAndSignStates;
}
