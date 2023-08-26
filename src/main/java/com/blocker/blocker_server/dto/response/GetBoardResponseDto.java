package com.blocker.blocker_server.dto.response;

import com.blocker.blocker_server.entity.BookmarkId;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class GetBoardResponseDto {
    private Long boardId;
    private String title;
    private String content;
    private String name;
    private String representImage;
    private Integer view;
    private Integer bookmarkCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<ImageDto> images;
    private String info;
    private Long contractId;
    private Boolean isWriter;
    private Boolean isBookmark;
}
