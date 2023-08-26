package com.blocker.blocker_server.dto.request;

import com.blocker.blocker_server.exception.InvalidRequestParameterException;
import lombok.Getter;

@Getter
public class SaveBookmarkRequestDto {

    private Long boardId;

    public void validateFieldsNotNull() {
        if(boardId == null)
            throw new InvalidRequestParameterException("Invalid boardId");
    }
}
