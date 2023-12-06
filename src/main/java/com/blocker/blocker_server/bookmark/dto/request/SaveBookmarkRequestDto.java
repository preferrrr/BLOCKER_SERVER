package com.blocker.blocker_server.bookmark.dto.request;

import com.blocker.blocker_server.commons.exception.InvalidRequestParameterException;
import lombok.Getter;

@Getter
public class SaveBookmarkRequestDto {

    private Long boardId;

    public void validateFieldsNotNull() {
        if(boardId == null)
            throw new InvalidRequestParameterException("Invalid boardId");
    }
}
