package com.blocker.blocker_server.bookmark.repository;

import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.bookmark.domain.Bookmark;
import com.blocker.blocker_server.bookmark.domain.BookmarkId;
import com.blocker.blocker_server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {
    boolean existsByUserAndBoard(User user, Board board);

    void deleteByUserAndBoard(User user, Board board);
}
