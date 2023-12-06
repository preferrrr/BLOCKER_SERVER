package com.blocker.blocker_server.board.repository;

import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.contract.domain.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    boolean existsByContract(Contract contract);
}
