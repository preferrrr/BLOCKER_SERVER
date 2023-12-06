package com.blocker.blocker_server.commons.exception;

import lombok.Getter;

@Getter
public class ExistsAgreementSignException extends RuntimeException{
    private final String NAME;

    public ExistsAgreementSignException(String message) {
        super(message);
        NAME = "ExistsProceededContractException";
    }
}
