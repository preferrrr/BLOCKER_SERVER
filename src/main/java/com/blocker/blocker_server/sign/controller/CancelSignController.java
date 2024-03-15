package com.blocker.blocker_server.sign.controller;

import com.blocker.blocker_server.commons.response.BaseResponse;
import com.blocker.blocker_server.sign.service.CancelSignService;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cancel-signs") //체결된 계약을 파기 관련 api
public class CancelSignController {

    private final CancelSignService cancelSignService;

    /**
     * 파기 계약 진행
     */
    @PostMapping("/contract/{contractId}")
    public ResponseEntity<BaseResponse> cancelContract(@AuthenticationPrincipal User user, @PathVariable("contractId") Long contractId) {

        cancelSignService.cancelContract(user, contractId);

        return new ResponseEntity<>(
                BaseResponse.of(HttpStatus.CREATED),
                HttpStatus.CREATED
        );
    }

    /**
     * 서명
     * /cancel-signs/cancel-contract/{contractId}
     */
    @PatchMapping("/cancel-contract/{contractId}")
    public ResponseEntity<BaseResponse> signContract(@AuthenticationPrincipal User user, @PathVariable("contractId") Long cancelContractId) {

        cancelSignService.signCancelContract(user, cancelContractId);

        return ResponseEntity.ok(BaseResponse.ok());
    }

}
