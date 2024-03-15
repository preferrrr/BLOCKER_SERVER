package com.blocker.blocker_server.sign.controller;

import com.blocker.blocker_server.commons.response.BaseResponse;
import com.blocker.blocker_server.sign.dto.request.ProceedSignRequestDto;
import com.blocker.blocker_server.sign.service.AgreementSignService;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agreement-signs")
public class AgreementSignController {

    private final AgreementSignService agreementSignService;

    // rest api 관점에서 /signs 관련 uri로 들어가는 것이 맞을까 ?
    // 계약서의 상태를 바꾸고 계약의 엔티티를 create 함
    // 계약서의 상태를 바꾼다는 것에 초점을 두면 PATCH /contracts/{contractId}로 할 수 있겠고,
    // 계약 테이블에 create 한다는 것에 초점을 두면 POST /signs가 될 수 있겠다.
    // 나는 SIGN 테이블에 초점을 둔다.

    /**
     * 계약 진행.
     * 참여자들을 초대하고, 계약서의 상태도 진행 중으로 바뀜.
     * /signs
     */
    @PostMapping("")
    public ResponseEntity<BaseResponse> proceedContract(@AuthenticationPrincipal User user, @RequestBody ProceedSignRequestDto request) {

        agreementSignService.proceedContract(user, request);

        return new ResponseEntity<>(
                BaseResponse.of(HttpStatus.CREATED),
                HttpStatus.CREATED
        );
    }


    /**
     * 서명
     * /signs/contract/{contractId}
     */
    @PatchMapping("/contract/{contractId}")
    public ResponseEntity<BaseResponse> signContract(@AuthenticationPrincipal User user, @PathVariable("contractId") Long contractId) {

        agreementSignService.signContract(user, contractId);

        return ResponseEntity.ok(BaseResponse.ok());
    }

    /**
     * 진행 중 계약 파기
     * /signs/contract/{contractId}
     */
    @DeleteMapping("/contract/{contractId}")
    public ResponseEntity<BaseResponse> breakContract(@AuthenticationPrincipal User user, @PathVariable("contractId") Long contractId) {

        agreementSignService.breakContract(user, contractId);

        return ResponseEntity.ok(BaseResponse.ok());

    }

}
