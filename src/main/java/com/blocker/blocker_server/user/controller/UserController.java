package com.blocker.blocker_server.user.controller;

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

        requestDto.validateFieldsNotNull();

        ResponseEntity response = userService.login(requestDto);

        return response;
    }

    @GetMapping("/reissue-token")
    public ResponseEntity<HttpHeaders> reissueToken(@RequestHeader("Cookie") String cookie) {

        if(cookie.isEmpty() || cookie.isBlank())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        HttpHeaders headers = userService.reissueToken(cookie);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchUserResponse>> searchUsers(@RequestParam("keyword") String keyword) {

        List<SearchUserResponse> result = userService.searchUsers(keyword);

        return new ResponseEntity(result, HttpStatus.OK);
    }

}
