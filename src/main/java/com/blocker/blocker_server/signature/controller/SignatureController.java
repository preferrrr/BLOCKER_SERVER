package com.blocker.blocker_server.signature.controller;

import com.blocker.blocker_server.commons.response.BaseResponse;
import com.blocker.blocker_server.commons.response.SingleResponse;
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
    public ResponseEntity<HttpHeaders> setSignature(@RequestPart("signature") MultipartFile file) throws IOException {

        return ResponseEntity.ok(
                signatureService.setSignature(file)
        );
    }

    @PatchMapping("")
    public ResponseEntity<BaseResponse> modifySignature(@RequestPart("signature") MultipartFile file) throws IOException {

        signatureService.modifySignature(file);

        return ResponseEntity.ok(BaseResponse.ok());
    }

    @GetMapping("")
    public ResponseEntity<SingleResponse<GetSignatureResponseDto>> getSignature() {

        return ResponseEntity.ok(
                SingleResponse.ok(signatureService.getSignature())
        );
    }
}
