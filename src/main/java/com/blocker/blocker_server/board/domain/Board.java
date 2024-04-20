package com.blocker.blocker_server.board.domain;

import com.blocker.blocker_server.Image.domain.Image;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.bookmark.domain.Bookmark;
import com.blocker.blocker_server.commons.BaseEntity;
import com.blocker.blocker_server.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "BOARD")
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @Builder
    private Board(User user, String title, String content, Integer bookmarkCount, Integer view, String representImage, String info, Contract contract) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.bookmarkCount = bookmarkCount;
        this.view = view;
        this.representImage = representImage;
        this.info = info;
        this.contract = contract;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    public static Board create(User user, String title, String content, String representImage, String info, Contract contract) {
        return Board.builder()
                .user(user)
                .title(title)
                .content(content)
                .bookmarkCount(0)
                .view(0)
                .representImage(representImage)
                .info(info)
                .contract(contract)
                .build();
    }

    public void updateBoard(String title, String content, String info, String representImage, Contract contract) {
        this.title = title;
        this.content = content;
        this.info = info;
        this.representImage = representImage;
        this.contract = contract;
    }

    public void addBookmarkCount() {
        this.bookmarkCount += 1;
    }

    public void subBookmarkCount() {
        this.bookmarkCount -= 1;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
