package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.Board;
import com.blocker.blocker_server.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {
    List<Board> getBoardList(Pageable pageable);

    Optional<Board> getBoard(Long boardId);

    List<Board> getBookmarkBoards(User user, Pageable pageable);

    List<Board> getMyBoards(User user, Pageable pageable);
}
