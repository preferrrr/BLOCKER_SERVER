package com.blocker.blocker_server.bookmark.dto.request;

import com.blocker.blocker_server.commons.exception.InvalidRequestParameterException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveBookmarkRequestDto {

    private Long boardId;

    @Builder
    public SaveBookmarkRequestDto(Long boardId) {
        this.boardId = boardId;
    }

    public void validateFieldsNotNull() {
        if(boardId == null)
            throw new InvalidRequestParameterException("Invalid boardId");
    }
}
