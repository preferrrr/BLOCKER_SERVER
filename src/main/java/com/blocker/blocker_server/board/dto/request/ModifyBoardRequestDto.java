package com.blocker.blocker_server.board.dto.request;

import com.blocker.blocker_server.commons.exception.InvalidRequestParameterException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ModifyBoardRequestDto {
    private String title;
    private String content;
    private String info;
    private String representImage; // 대표이미지
    private Long contractId;
    private List<Long> deleteImageIds;
    private List<String> addImageAddresses;

    @Builder
    public ModifyBoardRequestDto(String title, String content, String info, String representImage, Long contractId, List<Long> deleteImageIds, List<String> addImageAddresses) {
        this.title = title;
        this.content = content;
        this.info = info;
        this.representImage = representImage;
        this.contractId = contractId;
        this.deleteImageIds = deleteImageIds;
        this.addImageAddresses = addImageAddresses;
    }

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
