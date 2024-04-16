package com.blocker.blocker_server.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SaveBoardRequestDto {
    @NotBlank(message = "제목은 null 또는 공백일 수 없습니다.")
    private String title;
    @NotBlank(message = "제목은 null 또는 공백일 수 없습니다.")
    private String content;
    private String info;
    private String representImage;
    @NotNull(message = "계약서 인덱스는 null 또는 공백일 수 없습니다.")
    private Long contractId;
    private List<String> images;

    @Builder
    public SaveBoardRequestDto(String title, String content, String info, String representImage, Long contractId, List<String> images) {
        this.title = title;
        this.content = content;
        this.info = info;
        this.representImage = representImage;
        this.contractId = contractId;
        this.images = images;
    }

}
