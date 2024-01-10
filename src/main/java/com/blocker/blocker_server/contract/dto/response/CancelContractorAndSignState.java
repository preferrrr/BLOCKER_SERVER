package com.blocker.blocker_server.contract.dto.response;

import com.blocker.blocker_server.sign.domain.SignState;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CancelContractorAndSignState {
    private String contractor;
    private SignState signState;

    @Builder
    private CancelContractorAndSignState(String contractor, SignState signState) {
        this.contractor = contractor;
        this.signState = signState;
    }

    public static CancelContractorAndSignState of(String contractor, SignState signState) {
        return CancelContractorAndSignState.builder()
                .contractor(contractor)
                .signState(signState)
                .build();
    }
}
