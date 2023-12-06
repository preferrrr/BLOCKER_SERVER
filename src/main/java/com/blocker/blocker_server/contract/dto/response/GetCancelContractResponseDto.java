package com.blocker.blocker_server.contract.dto.response;

import com.blocker.blocker_server.contract.dto.response.ContractorAndSignState;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class GetCancelContractResponseDto {
    private Long cancelContractId;
    private Long contractId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<ContractorAndSignState> contractorAndSignStates;
}
