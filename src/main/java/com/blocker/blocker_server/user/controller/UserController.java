package com.blocker.blocker_server.user.controller;

import com.blocker.blocker_server.commons.response.ApiResponse;
import com.blocker.blocker_server.user.dto.request.LoginRequestDto;
import com.blocker.blocker_server.user.dto.response.SearchUserResponse;
import com.blocker.blocker_server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.blocker.blocker_server.commons.response.response_code.UserResponseCode.*;

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
    public ApiResponse<HttpHeaders> reissueToken(@RequestHeader("Cookie") String cookie) {

        return ApiResponse.of(
                userService.reissueToken(cookie),
                REISSUE_TOKEN
        );

    }

    @GetMapping("/search")
    public ApiResponse<SearchUserResponse> searchUsers(@RequestParam("keyword") String keyword) {

        return ApiResponse.of(
                userService.searchUsers(keyword),
                SEARCH_USERS
        );

    }

}
