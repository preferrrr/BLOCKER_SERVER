package com.blocker.blocker_server.dto.response;

import com.blocker.blocker_server.entity.SignState;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ContractorAndSignState {
    private String contractor;
    private SignState signState;
}
