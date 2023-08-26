package com.blocker.blocker_server.controller;

import com.blocker.blocker_server.dto.request.SaveBookmarkRequestDto;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
