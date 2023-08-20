package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.Board;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {
    List<Board> getBoards(Pageable pageable);
}
