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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

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

    @AfterEach
    void tearDown() {
        bookmarkRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
    }

    @DisplayName("북마크를 저장한다.")
    @Test
    void save() {

        /** given */

        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        Board board = Board.create(user, "testTitle", "testContent", "testImage", "testInfo", contract);
        boardRepository.save(board);

        /** when */

        Bookmark bookmark = Bookmark.create(user, board);
        bookmarkServiceSupport.save(bookmark);

        /** then */

        List<Bookmark> bookmarks = bookmarkRepository.findAll();

        assertThat(bookmarks).hasSize(1);
        assertThat(bookmarks.get(0).getUser().getEmail()).isEqualTo(user.getEmail());
        assertThat(bookmarks.get(0).getBoard().getBoardId()).isEqualTo(board.getBoardId());
    }

    @DisplayName("북마크를 저장할 때, 이미 북마크로 등록했었다면 IsAlreadyBookmakredException을 던진다.")
    @Test
    void checkIsBookmarked() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        Board board = Board.create(user, "testTitle", "testContent", "testImage", "testInfo", contract);
        boardRepository.save(board);

        Bookmark bookmark = Bookmark.create(user, board);
        bookmarkRepository.save(bookmark);

        /** when then */

        assertThatThrownBy(() -> bookmarkServiceSupport.checkIsBookmarked(user, board))
                .isInstanceOf(IsAlreadyBookmarkedException.class);
    }

    @DisplayName("북마크를 삭제할 때, 북마크로 등록되어 있지 않다면 IsNotBookmarkedException을 던진다.")
    @Test
    void checkIsNotBookmarked() {

        /** given */

        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        Board board = Board.create(user, "testTitle", "testContent", "testImage", "testInfo", contract);
        boardRepository.save(board);

        /** when then */

        assertThatThrownBy(() -> bookmarkServiceSupport.checkIsNotBookmarked(user, board))
                .isInstanceOf(IsNotBookmarkedException.class);

    }

    @DisplayName("북마크를 삭제한다.")
    @Test
    void deleteBookmark() {

        /** given */

        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        Board board = Board.create(user, "testTitle", "testContent", "testImage", "testInfo", contract);
        boardRepository.save(board);

        Bookmark bookmark = Bookmark.create(user, board);
        bookmarkRepository.save(bookmark);

        /** when */

        bookmarkServiceSupport.deleteBookmark(user, board);

        /** then */

        List<Bookmark> bookmarks = bookmarkRepository.findAll();
        assertThat(bookmarks).isEmpty();
    }

    @DisplayName("북마크한 게시글을 조회한다.")
    @Test
    void getBookmarkBoards() {

        /** given */

        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);

        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);

        Board board = Board.create(user, "testTitle", "testContent", "testImage", "testInfo", contract);
        Board board2 = Board.create(user, "testTitle", "testContent", "testImage", "testInfo", contract);
        Board board3 = Board.create(user, "testTitle", "testContent", "testImage", "testInfo", contract);

        boardRepository.saveAll(List.of(board, board2, board3));

        Bookmark bookmark = Bookmark.create(user, board);
        Bookmark bookmark2 = Bookmark.create(user, board2);
        Bookmark bookmark3 = Bookmark.create(user, board3);
        bookmarkRepository.saveAll(List.of(bookmark, bookmark2, bookmark3));

        /** when */

        List<Board> result = bookmarkServiceSupport.getBookmarkBoards(user, PageRequest.of(0, 10));

        /** then */
        assertThat(result).hasSize(3);

        assertThat(result.get(0).getBoardId()).isEqualTo(board.getBoardId());
        assertThat(result.get(1).getBoardId()).isEqualTo(board2.getBoardId());
        assertThat(result.get(2).getBoardId()).isEqualTo(board3.getBoardId());

    }

}