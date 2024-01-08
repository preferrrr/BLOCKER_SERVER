package com.blocker.blocker_server.board.service;

import com.blocker.blocker_server.IntegrationTestSupport;
import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.Image.domain.Image;
import com.blocker.blocker_server.board.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.board.exception.UnauthorizedDeleteBoardException;
import com.blocker.blocker_server.board.exception.UnauthorizedModifyBoardException;
import com.blocker.blocker_server.board.repository.BoardRepository;
import com.blocker.blocker_server.Image.repository.ImageRepository;
import com.blocker.blocker_server.bookmark.domain.Bookmark;
import com.blocker.blocker_server.bookmark.repository.BookmarkRepository;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.repository.ContractRepository;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.repository.UserRepository;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class BoardServiceSupportTest extends IntegrationTestSupport {

    @Autowired
    private BoardServiceSupport boardServiceSupport;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @AfterEach
    void tearDown() {
        imageRepository.deleteAllInBatch();
        bookmarkRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        contractRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }


    @DisplayName("게시글 인덱스로 게시글을 조회한다.")
    @Test
    void getBoardById() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        Contract contract = Contract.create(user, "testTitle", "testContent");
        Board board = Board.create(user, "testTitle", "testContent", "testRepImage", "testInfo", contract);
        userRepository.save(user);
        contractRepository.save(contract);
        Board savedBoard = boardRepository.save(board);

        /** when */

        Board getBoard = boardServiceSupport.getBoardById(savedBoard.getBoardId());

        /** then */

        assertThat(getBoard.getBoardId()).isEqualTo(savedBoard.getBoardId());

    }

    @DisplayName("게시글 리스트를 조회한다.")
    @Test
    void getBoardList() {

        /** given */

        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        Contract contract = Contract.create(user, "testTitle", "testContent");
        Board board1 = Board.create(user, "testTitle", "testContent", "testRepImage", "testInfo", contract);
        Board board2 = Board.create(user, "testTitle", "testContent", "testRepImage", "testInfo", contract);
        Board board3 = Board.create(user, "testTitle", "testContent", "testRepImage", "testInfo", contract);
        userRepository.save(user);
        contractRepository.save(contract);
        boardRepository.saveAll(List.of(board1, board2, board3));

        /** when */
        List<Board> boards = boardServiceSupport.getBoardList(PageRequest.of(0, 10));

        /** then */
        assertThat(boards).hasSize(3);
    }

    @Test
    void getBoardWithImages() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        Contract contract = Contract.create(user, "testTitle", "testContent");
        Board board = Board.create(user, "testTitle", "testContent", "testRepImage", "testInfo", contract);
        Image image1 = Image.create(board, "testAddress1");
        Image image2 = Image.create(board, "testAddress2");
        Image image3 = Image.create(board, "testAddress3");
        userRepository.save(user);
        contractRepository.save(contract);
        Board savedBoard = boardRepository.save(board);
        imageRepository.saveAll(List.of(image1, image2, image3));

        /** when */

        Board getBoard = boardServiceSupport.getBoardWithImages(savedBoard.getBoardId());

        /** then */

        assertTrue(Hibernate.isInitialized(getBoard.getImages()));
        assertThat(getBoard.getImages().size()).isEqualTo(3);

        /** assertThat(board).isEqualTo(savedBoard);
         *  assertThat(board).isEqualTo(getBoard);
         *
         * @Transactional이 있으면, board == savedBoard == getBoard가 된다.
         * 메소드가 시작되는 순간부터 트랜잭션이 열리기 때문에 getBoardWithImages()로 조회했을 때,
         * getBoard는 1차 캐시에 있는 board가 됨.
         * => 위 코드에서 board.images의 연관관계를 설정해주지 않았기 때문에,
         * assertThat(getBoard.getImages.size()).isEqualTo(3)에서 오류가 터짐.
         *
         * but
         * @Transactional이 없으면, itemRepository를 저장 후 트랜잭션이 닫히고, BoardServiceSupport의 다른 트랜잭션이 열리기 때문에
         * board == savedBoard이고 board != getBoard 라서, 테스트가 통과하게 됨.
         *  */


    }

    @DisplayName("게시글을 저장한다.")
    @Test
    void save() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        Contract contract = Contract.create(user, "testTitle", "testContent");
        userRepository.save(user);
        contractRepository.save(contract);

        Board newBoard = Board.create(user, "testTitle", "testContent", "testImage", "testInfo", contract);

        /** when */
        boardServiceSupport.save(newBoard);

        /** then */

        List<Board> boards = boardRepository.findAll();
        assertThat(boards).hasSize(1)
                .extracting("title", "content")
                .containsExactlyInAnyOrder(
                        tuple("testTitle", "testContent")
                );

    }

    @DisplayName("게시글 리스트를 dto 리스트로 변환한다.")
    @Test
    void createBoardListResponseDto() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        Contract contract = Contract.create(user, "testTitle", "testContent");
        userRepository.save(user);
        contractRepository.save(contract);

        Board board1 = Board.create(user, "testTitle1", "testContent1", "testImage", "testInfo", contract);
        Board board2 = Board.create(user, "testTitle2", "testContent2", "testImage", "testInfo", contract);
        Board board3 = Board.create(user, "testTitle3", "testContent3", "testImage", "testInfo", contract);
        boardRepository.saveAll(List.of(board1, board2, board3));

        /** when */

        List<GetBoardListResponseDto> response = boardServiceSupport.createBoardListResponseDto(List.of(board1, board2, board3));

        /** then */

        assertThat(response).hasSize(3)
                .extracting("title", "content")
                .containsExactlyInAnyOrder(
                        tuple("testTitle1", "testContent1"),
                        tuple("testTitle2", "testContent2"),
                        tuple("testTitle3", "testContent3")
                );

    }

    @DisplayName("게시글 작성자가 맞으면 true를 반환한다.")
    @Test
    void isWriter() {

        /** given */
        User me = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        userRepository.saveAll(List.of(me, user2));

        Contract contract = Contract.create(me, "testTitle", "testContent");
        contractRepository.save(contract);

        Board board = Board.create(me, "testTitle1", "testContent1", "testImage", "testInfo", contract);
        boardRepository.save(board);

        /** when */

        boolean result = boardServiceSupport.checkIsWriter(me.getEmail(), board.getUser().getEmail());

        /** then */
        assertTrue(result);

    }


    @DisplayName("게시글 작성자가 아니면 false를 반환한다.")
    @Test
    void isNotWriter() {

        /** given */
        User me = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        userRepository.saveAll(List.of(me, user2));

        Contract contract = Contract.create(me, "testTitle", "testContent");
        contractRepository.save(contract);

        Board board = Board.create(me, "testTitle1", "testContent1", "testImage", "testInfo", contract);
        boardRepository.save(board);

        /** when */

        boolean result = boardServiceSupport.checkIsWriter(user2.getEmail(), board.getUser().getEmail());

        /** then */
        assertFalse(result);

    }

    @DisplayName("이 게시글을 북마크 했으면 true를 반환한다.")
    @Test
    void IsBookmarked() {

        /** given */
        User user = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        userRepository.save(user);

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        Board board = Board.create(user, "testTitle1", "testContent1", "testImage", "testInfo", contract);
        boardRepository.save(board);

        Bookmark bookmark = Bookmark.create(user, board);
        bookmarkRepository.save(bookmark);

        /** when */

        boolean result = boardServiceSupport.checkIsBookmarked(user, board);

        /** then */

        assertTrue(result);

    }

    @DisplayName("이 게시글을 북마크 하지 않았으면 false를 반환한다.")
    @Test
    void IsNotBookmarked() {

        /** given */
        User user = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User me = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        userRepository.saveAll(List.of(user, me));

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        Board board = Board.create(user, "testTitle1", "testContent1", "testImage", "testInfo", contract);
        boardRepository.save(board);

        Bookmark bookmark = Bookmark.create(user, board);
        bookmarkRepository.save(bookmark);

        /** when */

        boolean result = boardServiceSupport.checkIsBookmarked(me, board);

        /** then */

        assertFalse(result);

    }

    @DisplayName("삭제할 때 게시글 작성자가 아니면 UnAuthorizedDeleteBoardException을 던진다.")
    @Test
    void NohaveDeleteAuthority() {

        /** given */
        User user = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User me = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        userRepository.saveAll(List.of(user, me));

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        Board board = Board.create(user, "testTitle1", "testContent1", "testImage", "testInfo", contract);
        boardRepository.save(board);

        /** when then */

        assertThatThrownBy(() -> boardServiceSupport.checkDeleteAuthority(me.getEmail(), board.getUser().getEmail()))
                .isInstanceOf(UnauthorizedDeleteBoardException.class);

    }


    @DisplayName("수정할 때 게시글 작성자가 아니면 UnAuthorizedModifyException을 던진다.")
    @Test
    void NoHaveModifyAuthority() {

        /** given */
        User user = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User me = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        userRepository.saveAll(List.of(user, me));

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        Board board = Board.create(user, "testTitle1", "testContent1", "testImage", "testInfo", contract);
        boardRepository.save(board);

        /** when then */
        assertThatThrownBy(() -> boardServiceSupport.checkModifyAuthority(me.getEmail(), board.getUser().getEmail()))
                .isInstanceOf(UnauthorizedModifyBoardException.class);
    }

    @DisplayName("게시글을 삭제한다.")
    @Test
    void deleteBoardById() {

        /** given */
        User user = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        userRepository.save(user);

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        Board board = Board.create(user, "testTitle1", "testContent1", "testImage", "testInfo", contract);
        boardRepository.save(board);

        /** when */

        boardServiceSupport.deleteBoardById(board.getBoardId());

        /** then */

        List<Board> boards = boardRepository.findAll();
        assertThat(boards).hasSize(0);


    }

    @DisplayName("게시글에 포함된 계약서가 달라졌으면 바뀐 계약서를 반환한다.")
    @Test
    void modifyContractBelongingToBoard() {

        /** given */
        User user = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        userRepository.save(user);

        Contract contract1 = Contract.create(user, "testTitle1", "testContent1");
        Contract contract2 = Contract.create(user, "testTitle2", "testContent2");
        contractRepository.saveAll(List.of(contract1, contract2));

        Board board = Board.create(user, "testTitle1", "testContent1", "testImage", "testInfo", contract1);
        boardRepository.save(board);

        /** when */

        Contract result = boardServiceSupport.modifyContractBelongingToBoard(user.getEmail(), board, contract2.getContractId());

        /** then */

        assertThat(result.getContractId()).isEqualTo(contract2.getContractId());

    }

    @DisplayName("내가 작성한 게시글 리스트를 조회한다.")
    @Test
    void getMyBoards() {
        /** given */
        User me = User.create("testEmail1", "testName1", "testPicture",
                "testValue1", List.of("USER"));
        User user = User.create("testEmail2", "testName2", "testPicture",
                "testValue2", List.of("USER"));
        userRepository.saveAll(List.of(me, user));

        Contract contract1 = Contract.create(me, "testTitle1", "testContent1");
        Contract contract2 = Contract.create(user, "testTitle2", "testContent2");
        contractRepository.saveAll(List.of(contract1, contract2));

        Board board1 = Board.create(me, "testTitle1", "testContent1", "testImage", "testInfo", contract1);
        Board board2 = Board.create(me, "testTitle2", "testContent2", "testImage", "testInfo", contract1);
        Board board3 = Board.create(me, "testTitle3", "testContent3", "testImage", "testInfo", contract1);
        Board board4 = Board.create(user, "testTitle4", "testContent4", "testImage", "testInfo", contract2);
        Board board5 = Board.create(user, "testTitle5", "testContent5", "testImage", "testInfo", contract2);

        boardRepository.saveAll(List.of(board1, board2, board3, board4, board5));

        /** when */

        List<Board> result = boardServiceSupport.getMyBoards(me.getEmail(), PageRequest.of(0, 10));

        /** then */

        assertThat(result).hasSize(3);

    }
}