package com.blocker.blocker_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "BOARD")
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
}
