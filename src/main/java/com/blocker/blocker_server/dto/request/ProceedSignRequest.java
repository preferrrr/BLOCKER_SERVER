package com.blocker.blocker_server.dto.request;

import com.blocker.blocker_server.exception.InvalidRequestParameterException;
import lombok.Getter;

import java.util.List;

@Getter
public class ProceedSignRequest {
    private Long contractId;
    private List<String> contractors;

    public void validateFieldsNotNull() {
        if(contractId == null)
            throw new InvalidRequestParameterException("Invalid contractId");
        if(contractors.isEmpty())
            throw new InvalidRequestParameterException("Invalid contractors");
    }

}
