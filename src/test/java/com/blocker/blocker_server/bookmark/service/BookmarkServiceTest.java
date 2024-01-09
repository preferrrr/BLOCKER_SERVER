package com.blocker.blocker_server.bookmark.service;

import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.board.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.board.service.BoardServiceSupport;
import com.blocker.blocker_server.bookmark.domain.Bookmark;
import com.blocker.blocker_server.bookmark.dto.request.SaveBookmarkRequestDto;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @InjectMocks
    private BookmarkService bookmarkService;

    @Mock
    private BookmarkServiceSupport bookmarkServiceSupport;

    @Mock
    private BoardServiceSupport boardServiceSupport;

    private Board board;
    private User user;
    private Contract contract;

    @BeforeEach
    void setUp() {
        user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        contract = Contract.create(user, "testTitle", "testContent");
        board = Board.create(user, "testTitle", "testContent", "testImage", "testInfo", contract);

    }

    @DisplayName("게시글을 북마크한다.")
    @Test
    void saveBookmark() {

        /** given */
        BDDMockito.given(boardServiceSupport.getBoardById(anyLong())).willReturn(board);
        BDDMockito.willDoNothing().given(bookmarkServiceSupport).checkIsBookmarked(any(User.class),any(Board.class));
        BDDMockito.willDoNothing().given(bookmarkServiceSupport).save(any(Bookmark.class));

        SaveBookmarkRequestDto dto = SaveBookmarkRequestDto.builder().boardId(1l).build();

        /** when */

        bookmarkService.saveBookmark(user, dto);

        /** then */

        Mockito.verify(boardServiceSupport, Mockito.times(1)).getBoardById(anyLong());
        Mockito.verify(bookmarkServiceSupport, Mockito.times(1)).checkIsBookmarked(any(User.class),any(Board.class));
        Mockito.verify(bookmarkServiceSupport, Mockito.times(1)).save(any(Bookmark.class));
        assertThat(board.getBookmarkCount()).isEqualTo(1);

    }

    @DisplayName("게시글 북마크를 해제한다.")
    @Test
    void deleteBookmark() {

        /** given */
        BDDMockito.given(boardServiceSupport.getBoardById(anyLong())).willReturn(board);
        BDDMockito.willDoNothing().given(bookmarkServiceSupport).checkIsNotBookmarked(any(User.class),any(Board.class));
        BDDMockito.willDoNothing().given(bookmarkServiceSupport).deleteBookmark(any(User.class),any(Board.class));

        /** when */
        bookmarkService.deleteBookmark(user, 1l);

        /** then */

        Mockito.verify(boardServiceSupport, Mockito.times(1)).getBoardById(anyLong());
        Mockito.verify(bookmarkServiceSupport, Mockito.times(1)).checkIsNotBookmarked(any(User.class),any(Board.class));
        Mockito.verify(bookmarkServiceSupport, Mockito.times(1)).deleteBookmark(any(User.class), any(Board.class));

        assertThat(board.getBookmarkCount()).isEqualTo(-1);



    }

    @DisplayName("내가 북마크한 게시글을 조회한다.")
    @Test
    void getBookmarkBoards() {

        /** given */
        BDDMockito.given(bookmarkServiceSupport.getBookmarkBoards(any(User.class),any(Pageable.class))).willReturn(mock(List.class));
        BDDMockito.given(boardServiceSupport.createBoardListResponseDto(anyList())).willReturn(mock(List.class));

        /** when */

        bookmarkService.getBookmarkBoards(user,PageRequest.of(0,10));

        /** then */

        verify(bookmarkServiceSupport, times(1)).getBookmarkBoards(any(User.class),any(Pageable.class));
        verify(boardServiceSupport, times(1)).createBoardListResponseDto(anyList());

    }

}