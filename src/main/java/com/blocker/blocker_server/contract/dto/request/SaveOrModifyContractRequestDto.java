package com.blocker.blocker_server.contract.dto.request;

import com.blocker.blocker_server.commons.exception.InvalidRequestParameterException;
import lombok.Getter;

@Getter
public class SaveOrModifyContractRequestDto {
    private String title;
    private String content;

    public void validateFieldsNotNull() {
        if(title == null || title.isEmpty() || title.isBlank())
            throw new InvalidRequestParameterException("Invalid title");
        if(content == null || content.isEmpty() || content.isBlank())
            throw new InvalidRequestParameterException("Invalid content");
    }
}
