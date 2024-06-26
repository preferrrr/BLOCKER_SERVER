package com.blocker.blocker_server.bookmark.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveBookmarkRequestDto {

    @NotNull(message = "게시글 인덱스는 null 또는 공백일 수 없습니다.")
    private Long boardId;

    @Builder
    public SaveBookmarkRequestDto(Long boardId) {
        this.boardId = boardId;
    }

}
