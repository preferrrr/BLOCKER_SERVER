package com.blocker.blocker_server.service;

import com.blocker.blocker_server.dto.request.SaveBoardRequestDto;
import com.blocker.blocker_server.dto.request.SaveBookmarkRequestDto;
import com.blocker.blocker_server.entity.Board;
import com.blocker.blocker_server.entity.Bookmark;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.exception.DuplicateBookmarkException;
import com.blocker.blocker_server.exception.NotFoundException;
import com.blocker.blocker_server.repository.BoardRepository;
import com.blocker.blocker_server.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BoardRepository boardRepository;

    public void saveBookmark(User user, SaveBookmarkRequestDto requestDto) {
        Board board = boardRepository.findById(requestDto.getBoardId()).orElseThrow(() -> new NotFoundException("[save bookmark] boardId : " + requestDto.getBoardId()));

        if (bookmarkRepository.existsByUserAndBoard(user, board))
            throw new DuplicateBookmarkException("email : " + user.getEmail() + ", boardId: " + board.getBoardId());

        Bookmark bookmark = Bookmark.builder()
                .board(board)
                .user(user)
                .build();

        bookmarkRepository.save(bookmark);

        board.addBookmarkCount();

    }
}
