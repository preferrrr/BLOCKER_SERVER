package com.blocker.blocker_server.bookmark.service;

import com.blocker.blocker_server.IntegrationTestSupport;
import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.board.repository.BoardRepository;
import com.blocker.blocker_server.bookmark.domain.Bookmark;
import com.blocker.blocker_server.bookmark.exception.IsAlreadyBookmarkedException;
import com.blocker.blocker_server.bookmark.exception.IsNotBookmarkedException;
import com.blocker.blocker_server.bookmark.repository.BookmarkRepository;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.repository.ContractRepository;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertFalse;

class BookmarkServiceSupportTest extends IntegrationTestSupport {

    @Autowired
    private BookmarkServiceSupport bookmarkServiceSupport;

    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContractRepository contractRepository;

    private User me, anotherUser;
    private Contract anotherContract;
    private Board otherBoard1, otherBoard2, otherBoard3, notBookamrkedBoard;
    private Bookmark bookmark1, bookmark2, bookmark3;


    @BeforeEach
    void setUp() {
        me = User.create("my Email", "my Name", "my Picture", "my value", List.of("USER"));
        anotherUser = User.create("another Email", "another Name", "another Picture", "another value", List.of("USER"));
        userRepository.saveAll(List.of(me, anotherUser));

        anotherContract = Contract.create(anotherUser, "other contract title", "other contract content");
        contractRepository.save(anotherContract);

        otherBoard1 = Board.create(anotherUser, "other board1 title", "other board1 content", "other board1 rep image", "other board1 info", anotherContract);
        otherBoard2 = Board.create(anotherUser, "other board2 title", "other board2 content", "other board2 rep image", "other board2 info", anotherContract);
        otherBoard3 = Board.create(anotherUser, "other board3 title", "other board3 content", "other board3 rep image", "other board3 info", anotherContract);
        notBookamrkedBoard = Board.create(anotherUser, "not bookmakred board title", "not bookmakred board content", "not bookmakred board rep image", "not bookmakred board info", anotherContract);
        boardRepository.saveAll(List.of(otherBoard1, otherBoard2, otherBoard3, notBookamrkedBoard));

        bookmark1 = Bookmark.create(me, otherBoard1);
        bookmark2 = Bookmark.create(me, otherBoard2);
        bookmark3 = Bookmark.create(me, otherBoard3);
        bookmarkRepository.saveAll(List.of(bookmark1, bookmark2, bookmark3));
    }

    @AfterEach
    void tearDown() {
        bookmarkRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        contractRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("북마크를 저장한다.")
    @Test
    void save() {

        /** given */
        //before each

        /** when */

        Bookmark newBookmark = Bookmark.create(me, notBookamrkedBoard);
        bookmarkServiceSupport.save(newBookmark);

        /** then */

        List<Bookmark> bookmarks = bookmarkRepository.findAll();

        //before each 3개 + 새로운 북마크 1개 = 4개
        assertThat(bookmarks).hasSize(4);
    }

    @DisplayName("북마크를 저장할 때, 이미 북마크로 등록했었다면 IsAlreadyBookmakredException을 던진다.")
    @Test
    void checkIsBookmarked() {

        /** given */
        //before each
        /** when then */

        assertThatThrownBy(() -> bookmarkServiceSupport.checkIsBookmarked(me, otherBoard1))
                .isInstanceOf(IsAlreadyBookmarkedException.class);
    }

    @DisplayName("북마크를 삭제할 때, 북마크로 등록되어 있지 않다면 IsNotBookmarkedException을 던진다.")
    @Test
    void checkIsNotBookmarked() {

        /** given */
        //before each

        /** when then */

        assertThatThrownBy(() -> bookmarkServiceSupport.checkIsNotBookmarked(me, notBookamrkedBoard))
                .isInstanceOf(IsNotBookmarkedException.class);

    }

    @DisplayName("북마크를 삭제한다.")
    @Test
    void deleteBookmark() {

        /** given */
        //before each

        /** when */

        bookmarkServiceSupport.deleteBookmark(me, otherBoard1);

        /** then */

        assertFalse(bookmarkRepository.existsByUserAndBoard(me, otherBoard1));

    }

    @DisplayName("북마크한 게시글을 조회한다.")
    @Test
    void getBookmarkBoards() {

        /** given */
        //before each

        /** when */

        List<Board> result = bookmarkServiceSupport.getBookmarkBoards(me, PageRequest.of(0, 10));

        /** then */
        assertThat(result).hasSize(3);

    }

}