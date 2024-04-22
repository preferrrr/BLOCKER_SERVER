package com.blocker.blocker_server.board.repository;

import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.board.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.repository.ContractRepository;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class BoardQueryRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardQueryRepository boardQueryRepository;


    @BeforeEach
    public void setUp() {
        User user = User.create("email@email.com", "name", "picture", "value", List.of("USER"));
        Contract contract = Contract.create(user, "title", "content");

        List<Board> boards = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            Board board = Board.create(user, "title " + i, "content " + i, "image " + i, "info " + i, contract);
            boards.add(board);
        }

        userRepository.save(user);
        contractRepository.save(contract);
        boardRepository.saveAll(boards);
    }

    @AfterEach
    public void tearDown() {
        boardRepository.deleteAllInBatch();
        contractRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("게시글 리스트를 dto로 조회하고, dto의 필드들은 null이 아니다.")
    void 게시글_리스트_dto_조회() {

        //given
        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        List<GetBoardListResponseDto> result = boardQueryRepository.getBoardListDtoByQuery(pageRequest);

        //then

        assertThat(result.size()).isEqualTo(10);

        for (int i = 0; i < result.size(); i++) {
            GetBoardListResponseDto dto = result.get(0);

            assertThat(dto.getBoardId()).isNotNull();
            assertThat(dto.getName()).isNotNull();
            assertThat(dto.getTitle()).isNotNull();
            assertThat(dto.getContent()).isNotNull();
            assertThat(dto.getContractState()).isNotNull();
            assertThat(dto.getRepresentImage()).isNotNull();
            assertThat(dto.getBookmarkCount()).isNotNull();
            assertThat(dto.getView()).isNotNull();
            assertThat(dto.getCreatedAt()).isNotNull();
            assertThat(dto.getModifiedAt()).isNotNull();
        }


    }

    @Test
    @DisplayName("내가 쓴 게시글 리스트를 dto로 조회하고, dto의 필드들은 null이 아니다.")
    void 내가_쓴_게시글_리스트_dto_조회() {

        //given
        User me = User.create("email2@email.com", "name2", "picture", "my value", List.of("USER"));
        userRepository.save(me);

        Contract contract = Contract.create(me, "title", "content");
        contractRepository.save(contract);

        Board myBoard1 = Board.create(me, "title1", "content1", "image", "info", contract);
        Board myBoard2 = Board.create(me, "title2", "content1", "image", "info", contract);
        Board myBoard3 = Board.create(me, "title3", "content1", "image", "info", contract);
        boardRepository.saveAll(List.of(myBoard1, myBoard2, myBoard3));

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        List<GetBoardListResponseDto> result = boardQueryRepository.getMyBoardsByQuery(me.getEmail(), pageRequest);

        //then

        assertThat(result.size()).isEqualTo(3);

        for (int i = 0; i < result.size(); i++) {
            GetBoardListResponseDto dto = result.get(0);

            assertThat(dto.getBoardId()).isNotNull();
            assertThat(dto.getName()).isNotNull();
            assertThat(dto.getTitle()).isNotNull();
            assertThat(dto.getContent()).isNotNull();
            assertThat(dto.getContractState()).isNotNull();
            assertThat(dto.getRepresentImage()).isNotNull();
            assertThat(dto.getBookmarkCount()).isNotNull();
            assertThat(dto.getView()).isNotNull();
            assertThat(dto.getCreatedAt()).isNotNull();
            assertThat(dto.getModifiedAt()).isNotNull();
        }


    }
}