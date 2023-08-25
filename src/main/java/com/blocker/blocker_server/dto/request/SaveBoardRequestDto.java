package com.blocker.blocker_server.dto.request;

import com.blocker.blocker_server.exception.InvalidRequestParameterException;
import lombok.Getter;

import java.util.List;

@Getter
public class SaveBoardRequestDto {
    private String title;
    private String content;
    private String info;
    private String representImage;
    private Long contractId;
    private List<String> images;

    public void validateFieldsNotNull() {
        if(title == null || title.isEmpty() || title.isBlank())
            throw new InvalidRequestParameterException("Invalid title");
        if(content == null || content.isEmpty() || content.isBlank())
            throw new InvalidRequestParameterException("Invalid name");
        if(contractId == null)
            throw new InvalidRequestParameterException("Invalid contractId");
    }
}
