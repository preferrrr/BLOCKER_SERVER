package com.blocker.blocker_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetBoardsResponseDto {
    private Long boardId;
    private String title;
    private String name;
    private String content;
    private String representImage;
    private Integer view;
    private Integer bookmarkCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
