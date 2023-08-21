package com.blocker.blocker_server.controller;

import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.service.SignatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signatures")
public class SignatureController {

    private final SignatureService signatureService;

    @PostMapping("")
    public ResponseEntity<?> setSignature(@AuthenticationPrincipal User user, @RequestPart("signature") MultipartFile file) throws IOException {

        if(file.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        HttpHeaders headers = signatureService.setSignature(user, file);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
