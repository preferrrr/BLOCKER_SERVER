package com.blocker.blocker_server.entity;

import com.blocker.blocker_server.dto.request.ModifyBoardRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "BOARD")
@NoArgsConstructor
@DynamicInsert
public class Board extends BaseEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "bookmark_count", columnDefinition = "INT DEFAULT 0")
    private Integer bookmarkCount;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer view;

    @Column(name = "represent_image")
    private String representImage;

    private String info;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Builder
    public Board(User user, String title, String content, Integer bookmarkCount, Integer view, String representImage, String info) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.bookmarkCount = bookmarkCount;
        this.view = view;
        this.representImage = representImage;
        this.info = info;
    }
    
    public void updateBoard(ModifyBoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.info = requestDto.getInfo();
        this.representImage = requestDto.getRepresentImage();
        //TODO : 계약서
    }

    public void addBookmarkCount() {
        this.bookmarkCount += 1;
    }
}
