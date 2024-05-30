package com.blocker.blocker_server.bookmark.controller;

import com.blocker.blocker_server.bookmark.service.BookmarkService;
import com.blocker.blocker_server.bookmark.dto.request.SaveBookmarkRequestDto;
import com.blocker.blocker_server.board.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.commons.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static com.blocker.blocker_server.commons.response.response_code.BookmarkResponseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("")
    public ApiResponse<Void> saveBookmark(@RequestBody @Valid SaveBookmarkRequestDto requestDto) {

        bookmarkService.saveBookmark(requestDto);

        return ApiResponse.of(POST_BOOKMARK);

    }

    @DeleteMapping("/{boardId}")
    public ApiResponse<Void> deleteBookmark(@PathVariable("boardId") Long boardId) {

        bookmarkService.deleteBookmark(boardId);

        return ApiResponse.of(DELETE_BOOKMARK);
    }

    @GetMapping("/boards")
    public ApiResponse<GetBoardListResponseDto> getBookmarkBoards(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return ApiResponse.of(
                bookmarkService.getBookmarkBoards(pageable),
                GET_BOOKMARK_BOARDS
        );
    }
}
