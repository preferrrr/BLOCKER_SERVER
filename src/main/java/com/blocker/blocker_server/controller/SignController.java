package com.blocker.blocker_server.controller;

import com.blocker.blocker_server.dto.request.ProceedSignRequest;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signs")
public class SignController {

    private final SignService signService;

    // rest api 관점에서 /signs 관련 uri로 들어가는 것이 맞을까 ?
    // 계약서의 상태를 바꾸고 계약의 엔티티를 create 함
    // 계약서의 상태를 바꾼다는 것에 초점을 두면 PATCH /contracts/{contractId}로 할 수 있겠고,
    // 계약 테이블에 create 한다는 것에 초점을 두면 POST /signs가 될 수 있겠다.
    // 나는 SIGN 테이블에 초점을 둔다.

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


    /**서명
     * /signs/{contractId}
     * */
    @PatchMapping("/{contractId}")
    public ResponseEntity<HttpStatus> signContract(@AuthenticationPrincipal User user, @PathVariable("contractId") Long contractId) {

        signService.signContract(user, contractId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
