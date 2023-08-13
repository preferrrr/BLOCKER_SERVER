package com.blocker.blocker_server.controller;

import com.blocker.blocker_server.dto.LoginRequestDto;
import com.blocker.blocker_server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto) {

        requestDto.validateFieldsNotNull();

        ResponseEntity response = userService.login(requestDto);

        return response;
    }


}
