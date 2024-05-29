package com.blocker.blocker_server.signature.controller;

import com.blocker.blocker_server.commons.response.ApiResponse;
import com.blocker.blocker_server.signature.dto.response.GetSignatureResponseDto;
import com.blocker.blocker_server.signature.service.SignatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.blocker.blocker_server.commons.response.response_code.SignatureResponseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signatures")
public class SignatureController {

    private final SignatureService signatureService;

    @PostMapping("")
    public ApiResponse<HttpHeaders> setSignature(@RequestPart("signature") MultipartFile file) throws IOException {

        return ApiResponse.of(signatureService.setSignature(file),
                POST_SIGNATURE
        );
    }

    @PatchMapping("")
    public ApiResponse modifySignature(@RequestPart("signature") MultipartFile file) throws IOException {

        signatureService.modifySignature(file);

        return ApiResponse.of(MODIFY_SIGNATURE);
    }

    @GetMapping("")
    public ApiResponse<GetSignatureResponseDto> getSignature() {

        return ApiResponse.of(
                signatureService.getSignature(),
                GET_SIGNATURE
        );
    }
}
