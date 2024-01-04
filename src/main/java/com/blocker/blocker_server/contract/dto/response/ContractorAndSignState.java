package com.blocker.blocker_server.contract.dto.response;

import com.blocker.blocker_server.sign.domain.SignState;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ContractorAndSignState {
    private String contractor;
    private SignState signState;

    @Builder
    private ContractorAndSignState(String contractor, SignState signState) {
        this.contractor = contractor;
        this.signState = signState;
    }

    public static ContractorAndSignState of(String contractor, SignState signState) {
        return ContractorAndSignState.builder()
                .contractor(contractor)
                .signState(signState)
                .build();
    }
}
