package com.blocker.blocker_server.signature.controller;

import com.blocker.blocker_server.signature.dto.response.GetSignatureResponseDto;
import com.blocker.blocker_server.signature.service.SignatureService;
import com.blocker.blocker_server.user.domain.User;
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
@RequestMapping("/signatures")
public class SignatureController {

    private final SignatureService signatureService;

    @PostMapping("")
    public ResponseEntity<HttpHeaders> setSignature(@AuthenticationPrincipal User user, @RequestPart("signature") MultipartFile file) throws IOException {

        if (file.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        HttpHeaders headers = signatureService.setSignature(user, file);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PatchMapping("")
    public ResponseEntity<HttpStatus> modifySignature(@AuthenticationPrincipal User user, @RequestPart("signature") MultipartFile file) throws IOException {

        if (file.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        signatureService.modifySignature(user, file);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<GetSignatureResponseDto> getSignature(@AuthenticationPrincipal User user) {

        GetSignatureResponseDto dto = signatureService.getSignature(user);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
