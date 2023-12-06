package com.blocker.blocker_server.contract.dto.response;

import com.blocker.blocker_server.sign.domain.SignState;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ContractorAndSignState {
    private String contractor;
    private SignState signState;
}
