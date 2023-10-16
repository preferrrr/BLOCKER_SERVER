package com.blocker.blocker_server.controller;

import com.blocker.blocker_server.dto.request.SaveBookmarkRequestDto;
import com.blocker.blocker_server.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("")
    public ResponseEntity<HttpStatus> saveBookmark(@AuthenticationPrincipal User user,
                                                   @RequestBody SaveBookmarkRequestDto requestDto) {
        requestDto.validateFieldsNotNull();

        bookmarkService.saveBookmark(user, requestDto);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<HttpStatus> deleteBookmark(@AuthenticationPrincipal User user,
                                                     @PathVariable("boardId") Long boardId) {
        bookmarkService.deleteBookmark(user, boardId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/boards")
    public ResponseEntity<List<GetBoardListResponseDto>> getBookmarkBoards(@AuthenticationPrincipal User user,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        List<GetBoardListResponseDto> response = bookmarkService.getBookmarkBoards(user, pageable);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
