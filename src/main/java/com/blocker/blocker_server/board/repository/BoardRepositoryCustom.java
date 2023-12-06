package com.blocker.blocker_server.board.repository;

import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.user.domain.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {
    List<Board> getBoardList(Pageable pageable);

    Optional<Board> getBoard(Long boardId);

    List<Board> getBookmarkBoards(User user, Pageable pageable);

    List<Board> getMyBoards(User user, Pageable pageable);
}
