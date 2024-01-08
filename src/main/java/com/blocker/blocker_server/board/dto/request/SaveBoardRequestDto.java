package com.blocker.blocker_server.board.dto.request;

import com.blocker.blocker_server.commons.exception.InvalidRequestParameterException;
import lombok.Builder;
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

    @Builder
    private SaveBoardRequestDto(String title, String content, String info, String representImage, Long contractId, List<String> images) {
        this.title = title;
        this.content = content;
        this.info = info;
        this.representImage = representImage;
        this.contractId = contractId;
        this.images = images;
    }




    public void validateFieldsNotNull() {
        if(title == null || title.isEmpty() || title.isBlank())
            throw new InvalidRequestParameterException("Invalid title");
        if(content == null || content.isEmpty() || content.isBlank())
            throw new InvalidRequestParameterException("Invalid name");
        if(contractId == null)
            throw new InvalidRequestParameterException("Invalid contractId");
    }
}
