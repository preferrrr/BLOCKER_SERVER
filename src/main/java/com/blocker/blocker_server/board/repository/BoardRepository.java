package com.blocker.blocker_server.board.repository;

import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.board.dto.response.GetBoardListResponseDtoInterface;
import com.blocker.blocker_server.contract.domain.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    boolean existsByContract(Contract contract);

    Optional<Board> findByBoardId(Long id);


    @Query(value = "SELECT b.board_id as boardId, b.title as title, u.name as name, b.content as content, b.represent_image as representImage, " +
            "b.view as view, b.bookmark_count as bookmarkCount, c.contract_state as contractState, b.created_at as createdAt, b.modified_at as modifiedAt " +
            "FROM board b use index (PRIMARY) " +
            "JOIN contract c ON b.contract_id = c.contract_id " +
            "JOIN users u ON b.user_id = u.email " +
            "ORDER BY b.board_id DESC " +
            "LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<GetBoardListResponseDtoInterface> getBoardListDtosWithUseIndex(@Param("limit") int limit, @Param("offset") int offset);

}
