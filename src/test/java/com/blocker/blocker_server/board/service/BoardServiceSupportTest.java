package com.blocker.blocker_server.board.service;

import com.blocker.blocker_server.IntegrationTestSupport;
import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.Image.domain.Image;
import com.blocker.blocker_server.board.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.board.exception.BoardNotFoundException;
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
import org.junit.jupiter.api.BeforeEach;
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


    private User me, anotherUser;
    private Contract myContract, anotherContract;
    private Board myBoard1, myBoard2, myBoard3, otherBoard1, otherBoard2;
    private Image image1, image2, image3;
    private Bookmark bookmark;

    @BeforeEach
    void setUp() {
        me = User.create("me@gmail.com", "me", "me picture", "me value", List.of("USER"));
        anotherUser = User.create("anotherUser@gmail.com", "another user", "another user picture", "another user value", List.of("USER"));

        myContract = Contract.create(me, "my contract title", "my contract content");
        anotherContract = Contract.create(anotherUser, "another contract title", "another contract content");

        myBoard1 = Board.create(me, "my board1 title", "my board1 content", "my rep image1", "my info1", myContract);
        myBoard2 = Board.create(me, "my board2 title", "my board2 content", "my rep image2", "my info2", myContract);
        myBoard3 = Board.create(me, "my board3 title", "my board3 content", "my rep image3", "my info3", myContract);
        otherBoard1 = Board.create(anotherUser, "other board1 title", "other board1 content", "other rep image1", "other info1", anotherContract);
        otherBoard2 = Board.create(anotherUser, "other board2 title", "other board2 content", "other rep image2", "other info2", anotherContract);

        image1 = Image.create(myBoard1, "address1");
        image2 = Image.create(myBoard1, "address2");
        image3 = Image.create(myBoard1, "address3");

        bookmark = Bookmark.create(me, otherBoard1);

        userRepository.saveAll(List.of(me, anotherUser));
        contractRepository.saveAll(List.of(myContract, anotherContract));
        boardRepository.saveAll(List.of(myBoard1, myBoard2, myBoard3, otherBoard1, otherBoard2));
        imageRepository.saveAll(List.of(image1, image2, image3));
        bookmarkRepository.save(bookmark);
    }

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
        //before each

        /** when */

        Board getBoard = boardServiceSupport.getBoardById(myBoard1.getBoardId());

        /** then */

        assertThat(getBoard.getBoardId()).isEqualTo(myBoard1.getBoardId());
        assertThat(getBoard.getUser().getEmail()).isEqualTo(myBoard1.getUser().getEmail());

    }

    @DisplayName("게시글 인덱스로 조회할 때 게시글이 없으면 BoardNotFoundException을 던진다.")
    @Test
    void getBoardByIdException() {

        /** given when */
        // before each

        /** then */
        assertThatThrownBy(() -> boardServiceSupport.getBoardById(100l))
                .isInstanceOf(BoardNotFoundException.class);

    }

    @DisplayName("게시글 리스트를 조회한다.")
    @Test
    void getBoardList() {

        /** given */
        //before each

        /** when */

        List<Board> boards = boardServiceSupport.getBoardList(PageRequest.of(0, 10));

        /** then */
        assertThat(boards).hasSize(5);
    }

    @Test
    void getBoardWithImages() {

        /** given */
        //before each

        /** when */

        Board getBoard = boardServiceSupport.getBoardWithImages(myBoard1.getBoardId());

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
        Board newBoard = Board.create(me, "testTitle", "testContent", "testImage", "testInfo", myContract);

        /** when */
        boardServiceSupport.save(newBoard);

        /** then */
        List<Board> boards = boardRepository.findAll();
        //before each 5개 + 새로 저장한 1개 = 6개
        assertThat(boards).hasSize(6);
    }

    @DisplayName("게시글 리스트를 dto 리스트로 변환한다.")
    @Test
    void createBoardListResponseDto() {

        /** given */
        //before each

        /** when */

        List<GetBoardListResponseDto> response = boardServiceSupport.createBoardListResponseDto(List.of(myBoard1, myBoard2, myBoard3));

        /** then */

        assertThat(response).hasSize(3)
                .extracting("title", "content")
                .containsExactlyInAnyOrder(
                        tuple("my board1 title", "my board1 content"),
                        tuple("my board2 title", "my board2 content"),
                        tuple("my board3 title", "my board3 content")
                );

    }

    @DisplayName("게시글 작성자가 맞으면 true를 반환한다.")
    @Test
    void isWriter() {

        /** given */
        //before each

        /** when */

        boolean result = boardServiceSupport.checkIsWriter(me.getEmail(), myBoard1.getUser().getEmail());

        /** then */
        assertTrue(result);

    }


    @DisplayName("게시글 작성자가 아니면 false를 반환한다.")
    @Test
    void isNotWriter() {

        /** given */
        //before each

        /** when */

        boolean result = boardServiceSupport.checkIsWriter(anotherUser.getEmail(), myBoard1.getUser().getEmail());

        /** then */
        assertFalse(result);

    }

    @DisplayName("이 게시글을 북마크 했으면 true를 반환한다.")
    @Test
    void IsBookmarked() {

        /** given */
        //before each

        /** when */

        boolean result = boardServiceSupport.checkIsBookmarked(me, otherBoard1);

        /** then */

        assertTrue(result);

    }

    @DisplayName("이 게시글을 북마크 하지 않았으면 false를 반환한다.")
    @Test
    void IsNotBookmarked() {

        /** given */
        //before each

        /** when */

        boolean result = boardServiceSupport.checkIsBookmarked(me, otherBoard2);

        /** then */

        assertFalse(result);

    }

    @DisplayName("삭제할 때 게시글 작성자가 아니면 UnAuthorizedDeleteBoardException을 던진다.")
    @Test
    void NohaveDeleteAuthority() {

        /** given */
        //before each

        /** when then */

        assertThatThrownBy(() -> boardServiceSupport.checkDeleteAuthority(me.getEmail(), otherBoard1.getUser().getEmail()))
                .isInstanceOf(UnauthorizedDeleteBoardException.class);

    }


    @DisplayName("수정할 때 게시글 작성자가 아니면 UnAuthorizedModifyException을 던진다.")
    @Test
    void NoHaveModifyAuthority() {

        /** given */


        /** when then */
        assertThatThrownBy(() -> boardServiceSupport.checkModifyAuthority(me.getEmail(), otherBoard1.getUser().getEmail()))
                .isInstanceOf(UnauthorizedModifyBoardException.class);
    }

    @DisplayName("게시글을 삭제한다.")
    @Test
    void deleteBoardById() {

        /** given */
        //before each

        /** when */

        boardServiceSupport.deleteBoardById(myBoard1.getBoardId());

        /** then */

        List<Board> boards = boardRepository.findAll();
        //before each의 5개 - 1개 = 4개
        assertThat(boards).hasSize(4);


    }

    @DisplayName("게시글에 포함된 계약서가 달라졌으면 바뀐 계약서를 반환한다.")
    @Test
    void modifyContractBelongingToBoard() {

        /** given */
        //before each
        Contract newContract = Contract.create(me, "new contract title", "new contract content");
        contractRepository.save(newContract);

        /** when */

        Contract result = boardServiceSupport.modifyContractBelongingToBoard(me.getEmail(), myBoard1, newContract.getContractId());

        /** then */

        assertThat(result.getContractId()).isEqualTo(newContract.getContractId());

    }

    @DisplayName("게시글에 포함된 계약서가 같을 경우 해당 계약서를 반환한다.")
    @Test
    void modifyContractBelongingToBoardWithSameContract() {

        /** given */
        //before each

        /** when */

        Contract result = boardServiceSupport.modifyContractBelongingToBoard(me.getEmail(), myBoard1, myContract.getContractId());

        /** then */

        assertThat(result.getContractId()).isEqualTo(myContract.getContractId());

    }

    @DisplayName("내가 작성한 게시글 리스트를 조회한다.")
    @Test
    void getMyBoards() {

        /** given */
        //before each

        /** when */

        List<Board> result = boardServiceSupport.getMyBoards(me.getEmail(), PageRequest.of(0, 10));

        /** then */

        assertThat(result).hasSize(3);

    }
}