package com.blocker.blocker_server.dto.response;

import com.blocker.blocker_server.entity.ContractState;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class GetBoardListResponseDto {
    private Long boardId;
    private String title;
    private String name;
    private String content;
    private String representImage;
    private Integer view;
    private Integer bookmarkCount;
    private ContractState contractState;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
