package com.blocker.blocker_server.service;

import com.blocker.blocker_server.dto.request.SaveBoardRequestDto;
import com.blocker.blocker_server.dto.request.SaveBookmarkRequestDto;
import com.blocker.blocker_server.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.entity.Board;
import com.blocker.blocker_server.entity.Bookmark;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.exception.DuplicateBookmarkException;
import com.blocker.blocker_server.exception.NotFoundException;
import com.blocker.blocker_server.repository.BoardRepository;
import com.blocker.blocker_server.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BoardRepository boardRepository;
    private final BoardService boardService;

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

    public void deleteBookmark(User user, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException("[delete bookmark] boardId : " + boardId));

        if(!bookmarkRepository.existsByUserAndBoard(user,board))
            throw new NotFoundException("[delete bookmark] boardId : " + boardId + ", email : " + user.getEmail());

        bookmarkRepository.deleteByUserAndBoard(user, board);

        board.subBookmarkCount();

    }

    public List<GetBoardListResponseDto> getBookmarkBoards(User me, Pageable pageable) {

        List<Board> boards = boardRepository.getBookmarkBoards(me, pageable);

        List<GetBoardListResponseDto> dtos = boardService.createDtos(boards);

        return dtos;

    }


}
