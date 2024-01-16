package com.blocker.blocker_server.sign.dto.request;

import com.blocker.blocker_server.commons.exception.InvalidRequestParameterException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProceedSignRequestDto {
    private Long contractId;
    private List<String> contractors;

    public void validateFieldsNotNull() {
        if(contractId == null)
            throw new InvalidRequestParameterException("Invalid contractId");
        if(contractors.isEmpty())
            throw new InvalidRequestParameterException("Invalid contractors");
    }

    @Builder
    public ProceedSignRequestDto(Long contractId, List<String> contractors) {
        this.contractId = contractId;
        this.contractors = contractors;
    }
}
