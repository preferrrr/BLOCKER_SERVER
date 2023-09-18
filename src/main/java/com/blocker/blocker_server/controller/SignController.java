package com.blocker.blocker_server.controller;

import com.blocker.blocker_server.dto.request.ProceedSignRequest;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.service.SignService;
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
@RequestMapping("/signs")
public class SignController {

    private final SignService signService;

    /**계약 진행.
     * 참여자들을 초대하고, 계약서의 상태도 진행 중으로 바뀜.
     * /signs
     * */
    @PostMapping("")
    public ResponseEntity<HttpStatus> proceedContract(@AuthenticationPrincipal User user, @RequestBody ProceedSignRequest request) {

        request.validateFieldsNotNull();

        signService.proceedContract(user, request);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
