package com.blocker.blocker_server.dto.request;

import com.blocker.blocker_server.exception.InvalidRequestParameterException;
import lombok.Getter;

import java.util.List;

@Getter
public class ModifyBoardRequestDto {
    private String title;
    private String content;
    private String info;
    private String representImage; // 대표이미지
    private Long contractId;
    private List<Long> deleteImageIds;
    private List<String> addImageAddresses;

    public void validateFieldsNotNull() {
        if(title == null || title.isEmpty() || title.isBlank())
            throw new InvalidRequestParameterException("Invalid title");
        if(content == null || content.isEmpty() || content.isBlank())
            throw new InvalidRequestParameterException("Invalid content");
        if(info == null || info.isEmpty() || info.isBlank())
            throw new InvalidRequestParameterException("Invalid info");
        if(contractId == null)
            throw new InvalidRequestParameterException("Invalid contractId");
    }

}
