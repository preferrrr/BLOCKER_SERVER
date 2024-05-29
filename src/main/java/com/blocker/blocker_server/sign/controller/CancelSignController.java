package com.blocker.blocker_server.sign.controller;

import com.blocker.blocker_server.commons.response.ApiResponse;
import com.blocker.blocker_server.sign.service.CancelSignService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.blocker.blocker_server.commons.response.response_code.CancelSignResponseCode.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cancel-signs") //체결된 계약을 파기 관련 api
public class CancelSignController {

    private final CancelSignService cancelSignService;

    /**
     * 파기 계약 진행
     */
    @PostMapping("/contract/{contractId}")
    public ApiResponse cancelContract(@PathVariable("contractId") Long contractId) {

        cancelSignService.cancelContract(contractId);

        return ApiResponse.of(POST_CANCEL_SIGN);
    }

    /**
     * 서명
     * /cancel-signs/cancel-contract/{contractId}
     */
    @PatchMapping("/cancel-contract/{contractId}")
    public ApiResponse signContract(@PathVariable("contractId") Long cancelContractId) {

        cancelSignService.signCancelContract(cancelContractId);

        return ApiResponse.of(SIGN_CANCEL_CONTRACT);
    }

}
