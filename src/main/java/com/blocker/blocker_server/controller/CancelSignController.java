package com.blocker.blocker_server.controller;

import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.service.CancelSignService;
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

    /**파기 계약 진행*/
    @PostMapping("/contract/{contractId}")
    public ResponseEntity<HttpStatus> cancelContract(@AuthenticationPrincipal User user, @PathVariable("contractId") Long contractId) {

        cancelSignService.cancelContract(user, contractId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**서명
     * /cancel-signs/contract/{contractId}
     * */
    @PatchMapping("/contract/{contractId}")
    public ResponseEntity<HttpStatus> signContract(@AuthenticationPrincipal User user, @PathVariable("contractId") Long cancelContractId) {

        cancelSignService.signCancelContract(user, cancelContractId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
