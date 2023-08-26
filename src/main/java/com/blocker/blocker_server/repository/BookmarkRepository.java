package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.Board;
import com.blocker.blocker_server.entity.Bookmark;
import com.blocker.blocker_server.entity.BookmarkId;
import com.blocker.blocker_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {
    boolean existsByUserAndBoard(User user, Board board);

    void deleteByUserAndBoard(User user, Board board);
}
