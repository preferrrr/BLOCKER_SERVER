package com.blocker.blocker_server.bookmark.service;

import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.board.repository.BoardRepository;
import com.blocker.blocker_server.bookmark.domain.Bookmark;
import com.blocker.blocker_server.bookmark.repository.BookmarkRepository;
import com.blocker.blocker_server.bookmark.exception.IsAlreadyBookmarkedException;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookmarkServiceSupport {

    private final BookmarkRepository bookmarkRepository;
    private final BoardRepository boardRepository;

    public void save(Bookmark bookmark) {
        bookmarkRepository.save(bookmark);
    }

    public void checkIsBookmarked(User user, Board board) {
        if(bookmarkRepository.existsByUserAndBoard(user, board))
            throw new IsAlreadyBookmarkedException("email : " + user.getEmail() + ", boardId : " + board.getBoardId());
    }

    public void checkIsNotBookmarked(User user, Board board) {
        if(bookmarkRepository.existsByUserAndBoard(user, board))
            throw new IsAlreadyBookmarkedException("email : " + user.getEmail() + ", boardId : " + board.getBoardId());
    }

    public void deleteBookmark(User user, Board board) {
        bookmarkRepository.deleteByUserAndBoard(user, board);
    }

    public List<Board> getBookmarkBoards(User me, Pageable pageable) {
        return boardRepository.getBookmarkBoards(me, pageable);
    }
}
