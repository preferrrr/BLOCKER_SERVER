package com.blocker.blocker_server.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    public Image(Board board, String imageAddress) {
        this.board = board;
        this.imageAddress = imageAddress;
    }
}
