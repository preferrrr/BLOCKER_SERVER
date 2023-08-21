package com.blocker.blocker_server.controller;

import com.blocker.blocker_server.dto.request.LoginRequestDto;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @PostMapping("/signature")
    public ResponseEntity<?> setSignature(@AuthenticationPrincipal User user, @RequestPart("signature") MultipartFile file) throws IOException {

        if(file.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        userService.setSignature(user, file);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/reissue-token")
    public ResponseEntity<?> reissueToken(@RequestHeader("Cookie") String cookie) {

        if(cookie.isEmpty() || cookie.isBlank())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        HttpHeaders headers = userService.reissueToken(cookie);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

}
