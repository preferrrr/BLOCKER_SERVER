package com.blocker.blocker_server.Image.domain;

import com.blocker.blocker_server.board.domain.Board;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "IMAGE")
@Getter
@NoArgsConstructor
public class Image {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Column(name = "image_address")
    private String imageAddress;

    @Builder
    private Image(Board board, String imageAddress) {
        this.board = board;
        this.imageAddress = imageAddress;
    }

    public static Image create(Board board, String imageAddress) {
        return Image.builder()
                .board(board)
                .imageAddress(imageAddress)
                .build();
    }
}
