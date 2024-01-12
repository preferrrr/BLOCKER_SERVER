package com.blocker.blocker_server.bookmark.service;

import com.blocker.blocker_server.board.service.BoardServiceSupport;
import com.blocker.blocker_server.bookmark.domain.Bookmark;
import com.blocker.blocker_server.bookmark.dto.request.SaveBookmarkRequestDto;
import com.blocker.blocker_server.board.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkServiceSupport bookmarkServiceSupport;
    private final BoardServiceSupport boardServiceSupport;

    @Transactional
    public void saveBookmark(User user, SaveBookmarkRequestDto requestDto) {

        //북마크할 게시글
        Board board = boardServiceSupport.getBoardById(requestDto.getBoardId());

        //이미 북마크했는지 검사
        bookmarkServiceSupport.checkIsBookmarked(user, board);

        //저장할 북마크 엔티티
        Bookmark bookmark = Bookmark.create(user, board);

        bookmarkServiceSupport.save(bookmark);

        //게시글의 북마크 개수 1 증가
        board.addBookmarkCount();

    }

    @Transactional
    public void deleteBookmark(User user, Long boardId) {

        //북마크 해제할 게시글
        Board board = boardServiceSupport.getBoardById(boardId);

        //북마크했는지 검사
        bookmarkServiceSupport.checkIsNotBookmarked(user, board);

        //북마크 삭제
        bookmarkServiceSupport.deleteBookmark(user, board);

        //게시글 북마크 개수 1 감소
        board.subBookmarkCount();

    }

    public List<GetBoardListResponseDto> getBookmarkBoards(User me, Pageable pageable) {

        //내가 북마크한 게시글 엔티티
        List<Board> boards = bookmarkServiceSupport.getBookmarkBoards(me, pageable);

        //dto로 변경
        return boardServiceSupport.createBoardListResponseDto(boards);

    }


}
