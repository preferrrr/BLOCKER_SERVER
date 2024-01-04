package com.blocker.blocker_server.board.dto.response;

import com.blocker.blocker_server.board.domain.Board;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
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

    @Builder
    private GetBoardResponseDto(Long boardId, String title, String content, String name, String representImage, Integer view, Integer bookmarkCount, LocalDateTime createdAt, LocalDateTime modifiedAt, List<ImageDto> images, String info, Long contractId, Boolean isWriter, Boolean isBookmark) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.name = name;
        this.representImage = representImage;
        this.view = view;
        this.bookmarkCount = bookmarkCount;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.images = images;
        this.info = info;
        this.contractId = contractId;
        this.isWriter = isWriter;
        this.isBookmark = isBookmark;
    }

    public static GetBoardResponseDto of(Board board, List<ImageDto> imageAddresses, boolean isWriter, boolean isBookmark) {
        return GetBoardResponseDto.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .name(board.getUser().getName())
                .content(board.getContent())
                .representImage(board.getRepresentImage())
                .view(board.getView())
                .bookmarkCount(board.getBookmarkCount())
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .images(imageAddresses)
                .info(board.getInfo())
                .contractId(board.getContract().getContractId())
                .isWriter(isWriter)
                .isBookmark(isBookmark)
                .build();
    }
}
