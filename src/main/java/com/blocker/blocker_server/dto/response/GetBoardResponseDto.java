package com.blocker.blocker_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetBoardResponseDto {
    private Long boardId;
    private String title;
    private String content;
    private String name;
    private String representImage;
    private Integer view;
    private Integer bookmarkCount;
    private List<String> images;
    private String info;
    private Long contractId;
    private boolean isWriter;
    private boolean isBookmark;
}
