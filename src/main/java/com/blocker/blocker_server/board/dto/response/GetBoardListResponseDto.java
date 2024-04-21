package com.blocker.blocker_server.board.dto.response;

import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.contract.domain.ContractState;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
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

    @Builder
    private GetBoardListResponseDto(Long boardId, String title, String name, String content, String representImage, Integer view, Integer bookmarkCount, ContractState contractState, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.boardId = boardId;
        this.title = title;
        this.name = name;
        this.content = content;
        this.representImage = representImage;
        this.view = view;
        this.bookmarkCount = bookmarkCount;
        this.contractState = contractState;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static GetBoardListResponseDto of(Board board) {
        return GetBoardListResponseDto.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .name(board.getUser().getName())
                .content(board.getContent())
                .representImage(board.getRepresentImage())
                .view(board.getView())
                .bookmarkCount(board.getBookmarkCount())
                .contractState(board.getContract().getContractState())
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .build();
    }
}
