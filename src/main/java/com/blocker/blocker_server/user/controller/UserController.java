package com.blocker.blocker_server.user.controller;

import com.blocker.blocker_server.commons.response.ListResponse;
import com.blocker.blocker_server.user.dto.request.LoginRequestDto;
import com.blocker.blocker_server.user.dto.response.SearchUserResponse;
import com.blocker.blocker_server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<HttpHeaders> login(@RequestBody LoginRequestDto requestDto) {

        return userService.login(requestDto);
    }

    @GetMapping("/reissue-token")
    public ResponseEntity<HttpHeaders> reissueToken(@RequestHeader("Cookie") String cookie) {

        return ResponseEntity.ok(
                userService.reissueToken(cookie)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ListResponse<SearchUserResponse>> searchUsers(@RequestParam("keyword") String keyword) {

        return ResponseEntity.ok(
                ListResponse.ok(userService.searchUsers(keyword))
        );
    }

}
